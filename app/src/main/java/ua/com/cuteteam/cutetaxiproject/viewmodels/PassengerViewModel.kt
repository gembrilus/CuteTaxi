package ua.com.cuteteam.cutetaxiproject.viewmodels

import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel
import com.google.android.gms.maps.model.CameraPosition
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.helpers.PhoneNumberHelper
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import java.util.*

class PassengerViewModel(
    private val repository: PassengerRepository,
    private val context: Context = AppClass.appContext()
) : BaseViewModel(repository) {

    private var dialogShowed = false

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    var cameraPosition: CameraPosition? = null

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || repository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
    }

    suspend fun currentCameraPosition(): CameraPosition {
        return cameraPosition ?: countryCameraPosition()
    }

    private suspend fun countryCameraPosition(): CameraPosition {
        val phone = AppSettingsHelper(context).phone
        val country = countryNameByRegionCode(
            PhoneNumberHelper().regionCode(phone!!)
        )
        val countryCoordinates = coordinatesByCountryName(country)
        return CameraPosition.builder().target(countryCoordinates).zoom(6f).build()
    }

    private suspend fun coordinatesByCountryName(countryName: String): LatLng {
        return GeocodeRequest.Builder().build()
            .requestCoordinatesByName(countryName).toLatLng()
    }

    private fun countryNameByRegionCode(regionCode: String): String {
        val local = Locale("", regionCode)
        return local.displayCountry
    }
}
