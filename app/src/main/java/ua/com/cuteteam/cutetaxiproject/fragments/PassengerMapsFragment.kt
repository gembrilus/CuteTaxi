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
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class PassengerMapsFragment : MapsFragment() {
    companion object {
        fun newInstance(googleMapOptions: GoogleMapOptions?) = PassengerMapsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("MapOptions", googleMapOptions)
            }
        }
    }

    override val viewModel: PassengerViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            PassengerViewModelFactory(PassengerRepository())
        )
            .get(PassengerViewModel::class.java)
    }

    override fun initMap(googleMapsHelper: GoogleMapsHelper) {
        permissionProvider?.withPermission(AccessFineLocationPermission()) {
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.addOnMapClickListener {
                    val markerData = viewModel.nextMarker(it)
                    when (markerData.first) {
                        "A" -> viewModel.setStartAddress(markerData.second.position)
                        "B" -> viewModel.setDestAddress(markerData.second.position)
                    }
                    viewModel.createMarker(markerData)
//                    viewModel.buildRoute()
                }
                val location = viewModel.locationProvider.getLocation()?.toLatLng ?: return@launch

                if (viewModel.markersData.value?.isEmpty() == true) {
                    viewModel.setMarkersData("A" to MarkerData(location, R.drawable.marker_a_icon))
                    viewModel.setStartAddress(location)
                } else viewModel.updateMapObjects()
            }
        }

        viewModel.startAddressData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.setMarkersData("A" to MarkerData(it, R.drawable.marker_a_icon))
                viewModel.buildRoute()
            } else {
                viewModel.removeMarker("A")
                viewModel.updateMapObjects()
            }
        })

        viewModel.destAddressData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.setMarkersData("B" to MarkerData(it, R.drawable.marker_b_icon))
                viewModel.buildRoute()
            } else {
                viewModel.removeMarker("B")
                viewModel.updateMapObjects()
            }
        })
    }
}