package ua.com.cuteteam.cutetaxiproject.repositories

import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.providers.LocationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.helpers.network.NetHelper
import ua.com.cuteteam.cutetaxiproject.providers.AuthProvider
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

open class Repository {

    val appContext = AppClass.appContext()
    val observableLocation =
        LocationLiveData()
    val locationProvider =
        LocationProvider()
    val routeBuilder = RouteProvider.Builder()
    val geocoder = GeocodeRequest.Builder()
    val netHelper = NetHelper(appContext)
    val spHelper = AppSettingsHelper(appContext)
    protected val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun signOut() = authProvider.signOutUser()
    fun getUser() = authProvider.user
    private val authProvider = AuthProvider()

}
