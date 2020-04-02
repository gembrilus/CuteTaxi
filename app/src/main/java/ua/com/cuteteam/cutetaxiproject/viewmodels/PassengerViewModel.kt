package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.findBy
import ua.com.cuteteam.cutetaxiproject.extentions.mutation
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.helpers.PhoneNumberHelper
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import java.io.IOException
import java.util.*

class PassengerViewModel(
    private val repository: Repository,
    private val context: Context = AppClass.appContext()
) : BaseViewModel(repository) {

    private val repo = repository as PassengerRepository

    private var dialogShowed = false

    val activeOrder: MutableLiveData<Order?>
        get() = repo.activeOrder


    val newOrder =
        MutableLiveData(Order(passengerId = FirebaseAuth.getInstance().currentUser!!.uid))

    val addresses = MutableLiveData<List<Address>>()

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    var cameraPosition: CameraPosition? = null

    var markers = MutableLiveData(mutableMapOf<Int, Marker?>())

    fun setMarkers(newMarkers: Map<Int, Marker?>) {
        markers.value = newMarkers.toMutableMap()
    }

    fun setMarker(key: Int, value: Marker?) {
        markers.value = markers.value?.plus(key to value)?.toMutableMap()
    }

    fun replaceMarkers(newMarkers: Map<Int, Marker?>) {
        markers.value?.clear()
        markers.value?.plusAssign(newMarkers)
    }

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || repository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
    }

    suspend fun currentCameraPosition(): CameraPosition {
        return cameraPosition ?: countryCameraPosition()
    }

    fun findMarkerByTag(tag: String): Marker? {
        return markers.value?.findBy { it.value?.tag == tag }?.value
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

    fun fetchCurrentAddress() = viewModelScope.launch {

        val coordinates = locationProvider.getLocation()

        if (coordinates != null) {

            val address =
                repo.geocoder.build().requestNameByCoordinates(coordinates.toLatLng).toAddress()
            newOrder.mutation {
                it.value?.addressStart = address
            }
        }
    }

    fun fetchAddresses(value: String) = viewModelScope.launch {

        val list = mutableListOf<Address>()

        withContext(Dispatchers.IO) {
            try {
                val geocodeResults = repo.geocoder.build().requestCoordinatesByName(value).results

                for (result in geocodeResults) {
                    list.add(
                        Address(
                            address = result.formattedAddress,
                            location = Coordinates(
                                result.geometry.location.latitude,
                                result.geometry.location.longitude
                            )
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    addresses.mutation { it.value = list }
                }

            } catch (exception: IOException) {
                Log.e("Geocoding error", exception.message.toString())
            }
        }
    }

    fun makeOrder() {
        if (newOrder.value!!.isReady() &&
            activeOrder.value == null
        ) {
            repo.makeOrder(newOrder.value!!)
        }
    }
}
