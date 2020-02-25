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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.SettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.ViewModelFactory

class TestActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
            setItemBackgroundResource(R.drawable.test_background)
            setBackgroundColor(resources.getColor(android.R.color.transparent))
        }

        val header = navigationView.getHeaderView(0).apply {
            setBackground(resources.getDrawable(R.drawable.test_background))
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
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}