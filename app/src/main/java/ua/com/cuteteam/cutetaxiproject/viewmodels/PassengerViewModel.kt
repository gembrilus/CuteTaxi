package ua.com.cuteteam.cutetaxiproject.viewmodels

import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel
import com.google.android.gms.maps.model.CameraPosition
import ua.com.cuteteam.cutetaxiproject.PhoneNumberValidator
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import java.util.*

class PassengerViewModel(private val repository: Repository) : BaseViewModel(repository) {

    private var dialogShowed = false

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || repository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
    }

    suspend fun startCameraPosition(): CameraPosition {
        val phone = AppSettingsHelper(context).phone
        val country = countryNameByRegionCode(PhoneNumberValidator().regionCode(phone!!))
        val coordinates = GeocodeRequest.Builder().build().requestCoordinatesByName(country)
        return CameraPosition.builder().target(coordinates.toLatLng()).zoom(7f).build()
    }

    private fun countryNameByRegionCode(regionCode: String): String {
        val local = Locale("", regionCode)
        return local.displayCountry
    }

}