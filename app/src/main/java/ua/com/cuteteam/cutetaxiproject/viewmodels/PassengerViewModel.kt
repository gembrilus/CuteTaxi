package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.MarkerData
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.extentions.mutation
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import java.io.IOException
import java.util.*

class PassengerViewModel(private val repository: Repository) : BaseViewModel(repository) {

    fun createOrUpdateMarkerByClick(
        tag: String,
        icon: Int,
        callback: ((Pair<String, MarkerData>) -> Unit)? = null
    ) {
        mapAction.value = MapAction.CreateMarkerByClick(tag, icon, callback)
    }

    fun stopMarkerUpdate() {
        mapAction.value = MapAction.StopMarkerUpdate()
    }
    private val repo = repository as PassengerRepository

    private var dialogShowed = false

    val activeOrder: MutableLiveData<Order?>
        get() = repo.activeOrder


    val newOrder =
        MutableLiveData(Order(passengerId = FirebaseAuth.getInstance().currentUser!!.uid))

    val addresses = MutableLiveData<List<Address>>()

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    private fun Order.isReady(): Boolean {
        return (this.passengerId != null &&
                this.addressStart?.location != null &&
                this.addressDestination?.location != null)
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
