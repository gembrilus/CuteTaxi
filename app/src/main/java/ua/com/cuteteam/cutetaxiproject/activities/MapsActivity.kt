package ua.com.cuteteam.cutetaxiproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ua.com.cuteteam.cutetaxiproject.R
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val permissionProvider = PermissionProvider(this)

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
                    val routeProvider = RouteProvider.Builder()
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


    @AfterPermissionGranted(PermissionProvider.LOCATION_REQUEST_CODE)
    private fun addAMarkerAndMoveTheCamera() {
        permissionProvider.withPermission(AccessFineLocationPermission()) {
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
