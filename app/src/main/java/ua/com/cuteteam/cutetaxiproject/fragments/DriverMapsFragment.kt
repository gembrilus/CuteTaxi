package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMapOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.helpers.GoogleMapsHelper
import ua.com.cuteteam.cutetaxiproject.permissions.AccessFineLocationPermission
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.BaseViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

class DriverMapsFragment : MapsFragment() {
    companion object {
        fun newInstance(googleMapOptions: GoogleMapOptions?) = DriverMapsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("MapOptions", googleMapOptions)
            }
        }
    }

    override val viewModel: DriverViewModel by lazy {
        ViewModelProvider(requireActivity(), BaseViewModel.getViewModelFactory(DriverRepository()))
            .get(DriverViewModel::class.java)
    }

    override fun restoreMapDataIfExist(googleMapsHelper: GoogleMapsHelper) {
        permissionProvider?.withPermission(AccessFineLocationPermission()) {
            GlobalScope.launch(Dispatchers.Main) {
                val location = viewModel.locationProvider.getLocation()?.toLatLng ?: return@launch
            }
        }
    }
}