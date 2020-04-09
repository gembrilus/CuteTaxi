package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMapOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.MarkerData
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.DriverViewModelFactory

class DriverMapsFragment : MapsFragment() {
    companion object {
        fun newInstance(googleMapOptions: GoogleMapOptions?) = DriverMapsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("MapOptions", googleMapOptions)
            }
        }
    }

    override val viewModel: DriverViewModel by lazy {
        ViewModelProvider(requireActivity(), DriverViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override fun initMap(googleMapsHelper: GoogleMapsHelper) {
        permissionProvider?.withPermission(AccessFineLocationPermission()) {
            GlobalScope.launch(Dispatchers.Main) {
                val location = viewModel.locationProvider.getLocation()?.toLatLng ?: return@launch

                viewModel.activeOrder.observe(this@DriverMapsFragment, Observer {
                    it ?: return@Observer

                    val startPoint = it.addressStart?.location?.toLatLng()!!
                    val destinationPoint = it.addressDestination?.location?.toLatLng()!!


                    viewModel.setMarkersData(
                        "A" to MarkerData(
                            startPoint,
                            R.drawable.marker_a_icon
                        )
                    )
                    viewModel.setMarkersData(
                        "B" to MarkerData(
                            destinationPoint,
                            R.drawable.marker_b_icon
                        )
                    )
                    viewModel.buildRoute(location, destinationPoint, listOf(startPoint))
                    viewModel.updateCameraForRoute()
                })
            }
        }
    }
}
