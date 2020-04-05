package ua.com.cuteteam.cutetaxiproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.dialogs.InfoDialog
import ua.com.cuteteam.cutetaxiproject.extentions.copy
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.permissions.PermissionProvider
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel

abstract class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        private var shouldShowPermissionPermanentlyDeniedDialog = true
    }

    abstract val viewModel: BaseViewModel

    var permissionProvider: PermissionProvider? = null

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

            viewModel.markers.observe(this@MapsFragment, Observer {
                googleMapsHelper
                    .addMarkers(viewModel.markers.value ?: emptyMap())
                    .also { viewModel.replaceMarkers(it) }
                GlobalScope.launch(Dispatchers.Main) {
                    googleMapsHelper.addPolyline(viewModel.polylineOptions ?: return@launch)
                }
            })

            viewModel.mapAction.observe(this@MapsFragment, Observer { mapAction ->
                when (mapAction) {
                    is MapAction.AddMarkers -> GlobalScope.launch(Dispatchers.Main) {
                        restoreMapDataIfExist(googleMapsHelper)
                    }
                    is MapAction.CreateMarkerByCoordinates -> {
                        googleMapsHelper.createMarkerByCoordinates(
                            mapAction.latLng,
                            mapAction.tag,
                            mapAction.icon
                        ).also { viewModel.setMarker(mapAction.icon, it) }
                    }

                    is MapAction.StartMarkerUpdate -> googleMapsHelper.createOrUpdateMarkerByClick(
                        viewModel.markers.value!!,
                        mapAction.tag,
                        mapAction.icon,
                        mapAction.callback
                    )
                    is MapAction.StopMarkerUpdate -> googleMapsHelper.removeOnMapClickListener()
                    is MapAction.BuildRoute -> GlobalScope.launch(Dispatchers.Main) {
                        viewModel.polylineOptions = googleMapsHelper.buildRoute(
                            viewModel.currentRoute ?: googleMapsHelper.routeSummery(
                                mapAction.from,
                                mapAction.to
                            )
                        )
                    }
                    is MapAction.UpdateCameraForRoute -> googleMapsHelper.updateCameraForCurrentRoute()
                    is MapAction.MoveCamera -> GlobalScope.launch(Dispatchers.Main) {
                        googleMapsHelper.moveCameraToLocation(mapAction.latLng)
                    }
                }
            })

            GlobalScope.launch(Dispatchers.Main) {
                if (viewModel.cameraPosition == null)
                    googleMapsHelper.moveCameraToLocation(
                        viewModel.locationProvider.getLocation()?.toLatLng ?: return@launch
                    )
            }

            googleMapsHelper.onCameraMove { position ->
                viewModel.cameraPosition = position
            }
        }
    }

    abstract fun restoreMapDataIfExist(googleMapsHelper: GoogleMapsHelper)

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
