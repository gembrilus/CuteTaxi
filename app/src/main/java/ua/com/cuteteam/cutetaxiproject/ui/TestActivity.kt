package ua.com.cuteteam.cutetaxiproject.ui

import android.os.Bundle
import android.view.MenuItem
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
import androidx.preference.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.activity_test.*
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreference
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreferenceDialogFragmentCompat
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.SettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.ViewModelFactory

class TestActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener
{

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    protected val model by lazy {
        ViewModelProvider(this, ViewModelFactory(this)).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setupWithNavController(navigationView, navController)

        navigationView.apply {
            setNavigationItemSelectedListener(this@TestActivity)
            background = resources.getDrawable(R.drawable.test_background)
/*            setItemBackgroundResource(android.R.color.white)
            setBackgroundColor(resources.getColor(android.R.color.transparent))*/
        }

        val header = navigationView.getHeaderView(0).apply {
            setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        }

        header.findViewById<SwitchMaterial>(R.id.role_chooser).apply {


            model.role.observe(this@TestActivity, Observer {
                text = when (it) {
                    0 -> "You are the passenger now"
                    else -> "You are the driver now"
                }
            })

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    model.setRole(1)
                } else model.setRole(0)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        item.isChecked = true

        if (item.itemId == R.id.settings){
            navigationView.menu.clear()
            navigationView.inflateMenu(R.menu.nav_menu_settings)
        } else drawerLayout.closeDrawers()

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.main_menu)
    }

}