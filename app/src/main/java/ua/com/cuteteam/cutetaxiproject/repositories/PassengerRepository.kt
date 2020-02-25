package ua.com.cuteteam.cutetaxiproject.repositories

import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider

class PassengerRepository {

    val observableLocation = LocationLiveData()

    val locationProvider = LocationProvider()

}
