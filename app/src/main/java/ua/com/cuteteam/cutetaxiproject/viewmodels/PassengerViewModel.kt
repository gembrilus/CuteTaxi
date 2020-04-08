package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.MarkerData
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.mutation
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import java.io.IOException

class PassengerViewModel(private val repository: PassengerRepository) : BaseViewModel(repository) {

    fun nextMarker(latLng: LatLng): Pair<String, MarkerData> {
        return if (findMarkerDataByTag("A") == null)
            "A" to MarkerData(latLng, R.drawable.marker_a_icon)
        else "B" to MarkerData(latLng, R.drawable.marker_b_icon)
    }

    fun createMarker(pair: Pair<String, MarkerData>) {
        mapAction.value = MapAction.CreateMarker(pair)
    }

    fun addMarkerByTag(tag: String, location: LatLng) {
        val markers: MutableMap<String, MarkerData> = markersData.value ?: mutableMapOf()
        when (tag) {
            "A" -> markers[tag] = MarkerData(location, R.drawable.marker_a_icon)
            "B" -> markers[tag] = MarkerData(location, R.drawable.marker_b_icon)
        }
        markersData.value = markers
        updateMapObjects()
    }

    fun addOnMapClickListener(callback: ((LatLng) -> Unit)) {
        mapAction.value = MapAction.AddOnMapClickListener(callback)
    }

    fun removeOnMapClickListener() {
        mapAction.value = MapAction.RemoveOnMapClickListener
    }

    val activeOrder: MutableLiveData<Order?>
        get() = repository.activeOrder


    val newOrder =
        MutableLiveData(Order(passengerId = FirebaseAuth.getInstance().currentUser!!.uid))

    val startAddressData: LiveData<LatLng?> =
        newOrder.map { it.addressStart?.location?.toLatLng() }
    val destAddressData: LiveData<LatLng?> =
        newOrder.map { it.addressDestination?.location?.toLatLng() }

    val addresses = MutableLiveData<List<Address>>()

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    fun setStartAddress(location: LatLng) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val address: Address = getAddressByLatLng(location)

            withContext(Dispatchers.Main) {
                newOrder.mutation { it.value?.addressStart = address }
            }
        }
    }

    fun setDestAddress(location: LatLng) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val address: Address = getAddressByLatLng(location)

            withContext(Dispatchers.Main) {
                newOrder.mutation { it.value?.addressDestination = address }
            }
        }
    }

    suspend fun getLocationByName(address: String): LatLng? {
        val results = repository.geocoder.build().requestCoordinatesByName(address).results
        return if (!results.isNullOrEmpty()) {
            results.first().geometry.location
        } else null
    }

    private suspend fun getAddressByLatLng(location: LatLng): Address {
        return repository.geocoder.build().requestNameByCoordinates(location)
            .toAddress()
    }

    fun fetchAddresses(value: String) = viewModelScope.launch {

        val list = mutableListOf<Address>()

        withContext(Dispatchers.IO) {
            try {
                val geocodeResults =
                    repository.geocoder.build().requestCoordinatesByName(value).results

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
            repository.makeOrder(newOrder.value!!)
        }
    }
}