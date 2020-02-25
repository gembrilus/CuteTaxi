package ua.com.cuteteam.cutetaxiproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.R
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.ui.settings.SettingsActivity
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val permissionProvider = PermissionProvider(this)

    private val passengerViewModel by lazy {
        ViewModelProvider(this, PassengerViewModelFactory(PassengerRepository()))
            .get(PassengerViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (passengerViewModel.shouldShowGPSRationale())
            InfoDialog.show(
                supportFragmentManager,
                getString(R.string.enable_gps_recommended_dialog_title),
                getString(R.string.enable_gps_recommended_dialog_message)
            )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addAMarkerAndMoveTheCamera()

        var isFirstClick = true
        var orig = ""
        var dest = ""

        mMap.setOnMapLongClickListener { latLng ->

            mMap.addMarker(MarkerOptions().position(latLng))

            val lat = latLng.latitude
            val lng = latLng.longitude

            GlobalScope.launch {

                withContext(Dispatchers.Main) {
                    if (isFirstClick) {
                        orig = "$lat,$lng"
                        isFirstClick = false
                    } else {
                        dest = "$lat,$lng"
                        isFirstClick = true
                    }
                }

                if (isFirstClick) {
                    val routeProvider = RouteProvider.Builder()   // Пример построения маршрута
                        .addOrigin(orig)
                        .addDestination(dest)
                        .build()

                    GlobalScope.launch(Dispatchers.Main) {
                        val value =
                            routeProvider.routes()
                        mMap.addPolyline(
                            PolylineOptions()
                                .clickable(true)
                                .add(*value[0].polyline)
                        )
                    }
                }

            }

        }
    }

/*    override fun onResume() {
        super.onResume()

        GlobalScope.launch {

            val geocode = GeocodeRequest.Builder()
                .build()
                .requestCoordinatesByName("Черкассы, ул. Шевченкоб 237")  // Пример Геокодинга

            val geocode2 = GeocodeRequest.Builder()
                .setLanguageResponse("us")
                .setRegion("us")
                .build()
                .requestNameByCoordinates("32.1231231,51.3333323")

            withContext(Dispatchers.Main) {
                InfoDialog.show(
                    supportFragmentManager, "Координаты", geocode.toLatLng().toString() + "\n" +
                            geocode2.toName()
                )
            }

        }

    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_settings -> startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            ).run { return true }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    @AfterPermissionGranted(PermissionProvider.LOCATION_REQUEST_CODE)
    private fun addAMarkerAndMoveTheCamera() {
        GlobalScope.launch(Dispatchers.Main) {
            val location = passengerViewModel.locationProvider.getLocation()
            location ?: return@launch

            val latLng = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title("My location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionProvider.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
