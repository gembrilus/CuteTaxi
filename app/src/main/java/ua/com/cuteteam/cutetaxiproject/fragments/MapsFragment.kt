package ua.com.cuteteam.cutetaxiproject.fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.permissions.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        private var shouldShowPermissionPermanentlyDeniedDialog = true

        fun newInstance(googleMapOptions: GoogleMapOptions?): MapsFragment {
            val mapsFragment = MapsFragment()
            var bundle: Bundle?
            Bundle().also { bundle = it }.putParcelable("MapOptions", googleMapOptions)
            mapsFragment.arguments = bundle
            return mapsFragment
        }
    }

    private lateinit var passengerViewModel: PassengerViewModel

    private var permissionProvider: PermissionProvider? = null

    private lateinit var currentLocation: Location

    private lateinit var mMap: GoogleMap
    
    private val accessFineLocationPermission = AccessFineLocationPermission()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        passengerViewModel = ViewModelProvider(
            activity!!, PassengerViewModelFactory(PassengerRepository())
        ).get(PassengerViewModel::class.java)

        permissionProvider = PermissionProvider(this).apply {
            onDenied = { permission, isPermanentlyDenied ->
                if (isPermanentlyDenied && shouldShowPermissionPermanentlyDeniedDialog) {
                    InfoDialog.show(
                        childFragmentManager,
                        permission.requiredPermissionDialogTitle,
                        permission.requiredPermissionDialogMessage
                    ) { shouldShowPermissionPermanentlyDeniedDialog = false }
                }
            }

            onGranted = {
                if (passengerViewModel.shouldShowGPSRationale())
                    InfoDialog.show(
                        childFragmentManager,
                        getString(R.string.enable_gps_recommended_dialog_title),
                        getString(R.string.enable_gps_recommended_dialog_message)
                    )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addAMarkerAndMoveTheCamera()
    }

    @AfterPermissionGranted(PermissionProvider.LOCATION_REQUEST_CODE)
    private fun addAMarkerAndMoveTheCamera() {
        ::mMap.isInitialized || return
        val googleMapsHelper = GoogleMapsHelper(mMap)
        permissionProvider?.withPermission(accessFineLocationPermission) {
            GlobalScope.launch(Dispatchers.Main) {
                passengerViewModel.markers.observe(this@MapsFragment, Observer {
                    googleMapsHelper
                        .addMarkers(passengerViewModel.markers.value ?: emptyMap())
                        .also { passengerViewModel.replaceMarkers(it) }
                })

                currentLocation = passengerViewModel.locationProvider.getLocation() ?: return@launch

                val latLng = latLng(currentLocation)
                googleMapsHelper.onCameraMove { position ->
                    passengerViewModel.cameraPosition = position
                }

                if (passengerViewModel.markers.value?.isEmpty() == true) {
                    val marker =
                        googleMapsHelper.createMarker(latLng, "A", R.drawable.marker_a_icon)
                    passengerViewModel.setMarker(R.drawable.marker_a_icon, marker)

                    passengerViewModel.markers.value = passengerViewModel.markers.value?.plus(
                        R.drawable.marker_a_icon to marker
                    )?.toMutableMap()
                }

                googleMapsHelper.createOrUpdateMarkerByClick(
                    passengerViewModel.markers.value!!,
                    "B",
                    R.drawable.marker_b_icon
                ) {
                    passengerViewModel.setMarker(R.drawable.marker_b_icon, it)
                }

                if (passengerViewModel.cameraPosition == null)
                    googleMapsHelper.moveCameraToLocation(latLng)
            }
        }
    }

    private fun latLng(currentLocation: Location) =
        LatLng(currentLocation.latitude, currentLocation.longitude)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionProvider?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
