package ua.com.cuteteam.cutetaxiproject.fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.permissions.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel

open class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

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

    open val viewModel by lazy {
        ViewModelProvider(this, BaseViewModel.getViewModelFactory(Repository()))
            .get(BaseViewModel::class.java)
    }

    private var permissionProvider: PermissionProvider? = null

    private lateinit var mMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
                if (viewModel.shouldShowGPSRationale())
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
        initMapData()
    }

    @AfterPermissionGranted(PermissionProvider.LOCATION_REQUEST_CODE)
    private fun initMapData() {
        ::mMap.isInitialized || return

        permissionProvider?.withPermission(AccessFineLocationPermission()) {
            val googleMapsHelper = GoogleMapsHelper(mMap)

            viewModel.mapAction.observe(this@MapsFragment, Observer { mapAction ->
                when (mapAction) {
                    is MapAction.AddMarkers -> googleMapsHelper.addMarkers(
                        viewModel.markers.value ?: emptyMap()
                    ).also { viewModel.replaceMarkers(it) }
                    is MapAction.StartMarkerUpdate -> googleMapsHelper.createOrUpdateMarkerByClick(
                        viewModel.markers.value!!,
                        mapAction.tag,
                        mapAction.icon,
                        mapAction.callback
                    )
                    is MapAction.StopMarkerUpdate -> googleMapsHelper.removeOnMapClickListener()
                    is MapAction.BuildRoute -> GlobalScope.launch(Dispatchers.Main) {
                        googleMapsHelper.buildRoute(
                            viewModel.currentRoute ?:  googleMapsHelper.routeSummery(
                                mapAction.from,
                                mapAction.to
                            )
                        )
                    }
                    is MapAction.UpdateCameraForRoute -> googleMapsHelper.updateCameraForCurrentRoute()
                    is MapAction.MoveCamera -> GlobalScope.launch {
                        googleMapsHelper.moveCameraToLocation(mapAction.latLng)
                    }
                }
            })

            GlobalScope.launch(Dispatchers.Main) {
                if (viewModel.cameraPosition == null)
                    googleMapsHelper.moveCameraToLocation(
                        viewModel.currentLocation.value ?: return@launch
                    )
            }

            googleMapsHelper.onCameraMove { position ->
                viewModel.cameraPosition = position
            }
        }
    }


//            GlobalScope.launch(Dispatchers.Main) {
//                passengerViewModel.markers.observe(this@MapsFragment, Observer {
//                    googleMapsHelper
//                        .addMarkers(passengerViewModel.markers.value ?: emptyMap())
//                        .also { passengerViewModel.replaceMarkers(it) }
//                    GlobalScope.launch(Dispatchers.Main) {
//                        googleMapsHelper.buildRoute(
//                            passengerViewModel.findMarkerByTag("A"),
//                            passengerViewModel.findMarkerByTag("B")
//                        )
//                    }
//                })
//
//                currentLocation = passengerViewModel.locationProvider.getLocation() ?: return@launch
//
//
//
//                if (passengerViewModel.markers.value?.isEmpty() == true) {
//                    val marker =
//                        googleMapsHelper.createMarker(currentLocation.toLatLng, "A", R.drawable.marker_a_icon)
//                    passengerViewModel.setMarker(R.drawable.marker_a_icon, marker)
//
//                    passengerViewModel.markers.value = passengerViewModel.markers.value?.plus(
//                        R.drawable.marker_a_icon to marker
//                    )?.toMutableMap()
//                }
//

//        }


    override fun onResume() {
        super.onResume()
        viewModel.addMarkers()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionProvider?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
