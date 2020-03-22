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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.permissions.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private var shouldShowPermissionPermanentlyDeniedDialog = true
    }

    private val permissionProvider = PermissionProvider(this).apply {
        onDenied = { permission, isPermanentlyDenied ->
            if (isPermanentlyDenied && shouldShowPermissionPermanentlyDeniedDialog) {
                InfoDialog.show(
                    supportFragmentManager,
                    permission.requiredPermissionDialogTitle,
                    permission.requiredPermissionDialogMessage
                ) { shouldShowPermissionPermanentlyDeniedDialog = false }
            }
        }
    }

    private val passengerViewModel by lazy {
        ViewModelProvider(this, PassengerViewModelFactory(PassengerRepository()))
            .get(PassengerViewModel::class.java)
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val role = AppSettingsHelper(this).role
        if (role) {
            startActivity(
                Intent(
                    this,
                    DriverActivity::class.java
                )
            )
        } else
            startActivity(
                Intent(
                    this,
                    PassengerActivity::class.java
                )
            )
        finish()

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
    }


    @AfterPermissionGranted(PermissionProvider.LOCATION_REQUEST_CODE)
    private fun addAMarkerAndMoveTheCamera() {
        permissionProvider.withPermission(AccessFineLocationPermission()) {
            GlobalScope.launch(Dispatchers.Main) {
                val location = passengerViewModel.locationProvider.getLocation()
                location ?: return@launch

                val latLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title("My location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
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
