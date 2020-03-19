package ua.com.cuteteam.cutetaxiproject.repositories

import androidx.room.FtsOptions
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.common.network.NetHelper
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class PassengerRepository {

    val appContext = AppClass.appContext()

    val observableLocation = LocationLiveData()

    val locationProvider = LocationProvider()
    val dao = PassengerDao()
    val routeBuilder = RouteProvider.Builder()
    val geocoder = GeocodeRequest.Builder()
    val netHelper = NetHelper(appContext)
    val spHelper = AppSettingsHelper(appContext)

    fun writeOrder(order: Order) {
        dao.writeOrder(order)
    }

}
