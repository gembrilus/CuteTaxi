package ua.com.cuteteam.cutetaxiproject.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.activities.AuthActivity
import ua.com.cuteteam.cutetaxiproject.common.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.permissions.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel

abstract class BaseActivity : AppCompatActivity() {

    abstract val menuResId: Int
    abstract val layoutResId: Int

    protected val permissionProvider get() = PermissionProvider(this).apply {
        onDenied = { permission, isPermanentlyDenied ->
            if (isPermanentlyDenied && model.shouldShowPermissionPermanentlyDeniedDialog) {
                InfoDialog.show(
                    supportFragmentManager,
                    permission.requiredPermissionDialogTitle,
                    permission.requiredPermissionDialogMessage
                ) { model.shouldShowPermissionPermanentlyDeniedDialog = false }
            }
        }
    }

    protected lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    protected lateinit var header: View

    private val model by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(PassengerRepository()))
            .get(BaseViewModel::class.java)
    }

    private val onNavigationListener = NavigationView.OnNavigationItemSelectedListener { item ->
        item.isChecked = true
        when(item.itemId){
            R.id.settings -> {
                navigationView.menu.clear()
                navigationView.inflateMenu(menuResId)
            }
            R.id.backToHome -> {
                navigationView.menu.clear()
                navigationView.inflateMenu(R.menu.main_menu)
                onBackPressed()
            }
            else -> drawerLayout.closeDrawers()
        }
        item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        initNavigation()
        initUI()
        setObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionProvider.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    private fun initNavigation() {

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        header = navigationView.getHeaderView(0)

        setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(onNavigationListener)

    }

    private fun initUI() {
        val headerName = header.findViewById<MaterialTextView>(R.id.tv_nav_header_name)
        val headerPhone = header.findViewById<MaterialTextView>(R.id.tv_nav_header_phone)
        val roleChooser = header.findViewById<SwitchMaterial>(R.id.role_chooser)

        with(AppSettingsHelper(this)) {
            headerName.text = name
            headerPhone.text = phone
        }

        with(roleChooser) {
            isChecked = model.isChecked
            setOnCheckedChangeListener { _, isChecked ->
                model.changeRole(isChecked)
                onRoleChanged()
            }
        }
    }

    private fun setObservers(){
        permissionProvider.withPermission(AccessFineLocationPermission()) {
                if (!model.isGPSEnabled) {
                    InfoDialog.show(
                        supportFragmentManager,
                        getString(R.string.enable_gps_recommended_dialog_title),
                        getString(R.string.enable_gps_recommended_dialog_message)
                    )
                }
        }

        model.netStatus.observe(this, Observer {
            when(it){
                NetStatus.AVAILABLE -> {}
                NetStatus.LOST, NetStatus.UNAVAILABLE -> {}
                else -> throw IllegalArgumentException("No such internet status! It is NULL")
            }
        })
    }

    private fun onRoleChanged() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

}