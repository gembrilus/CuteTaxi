package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMapOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.MapsFragment
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.ui.main.DriverActivity
import ua.com.cuteteam.cutetaxiproject.ui.main.PassengerActivity
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class MapsActivity : AppCompatActivity() {

    private val passengerViewModel by lazy {
        ViewModelProvider(this, PassengerViewModelFactory(PassengerRepository()))
            .get(PassengerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val options = GoogleMapOptions()
                options.camera(passengerViewModel.currentCameraPosition())

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.map_container, MapsFragment.newInstance(options))
                    .commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val role = AppSettingsHelper(this).role
                if (role) {
                    startActivity(
                        Intent(
                            this,
                            DriverActivity::class.java
                        )
                    ).run {
                        finish()
                        return true
                    }
                } else
                startActivity(
                    Intent(
                        this,
                        PassengerActivity::class.java
                    )
                ).run {
                    finish()
                    return true
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
