package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository

class PassengerViewModel(private val passengerRepository: PassengerRepository, private val context: Context = AppClass.appContext()) : ViewModel() {

    private var dialogShowed = false

    val observableLocation: LocationLiveData
        get() = passengerRepository.observableLocation

    val locationProvider: LocationProvider
        get() = passengerRepository.locationProvider

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || passengerRepository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
    }


}