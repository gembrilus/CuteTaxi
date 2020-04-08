package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
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
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import java.io.IOException

class PassengerViewModel(private val repository: PassengerRepository) : BaseViewModel(repository) {

    fun nextMarker(latLng: LatLng): Pair<String, MarkerData> {
        return if ( markersData.value?.isEmpty() == true )
            "A" to MarkerData(latLng, R.drawable.marker_a_icon)
        else "B" to MarkerData(latLng, R.drawable.marker_b_icon)
    }

    fun createMarker(pair: Pair<String, MarkerData>) {
        mapAction.value = MapAction.CreateMarker(pair)
    }

    fun addOnMapClickListener(callback: ((LatLng) -> Unit)) {
        mapAction.value = MapAction.AddOnMapClickListener( callback)
    }

    fun removeOnMapClickListener() {
        mapAction.value = MapAction.RemoveOnMapClickListener
    }

    fun removeMarker(tag: String) {
        mapAction.value = MapAction.RemoveMarker(tag)
    }

    private var dialogShowed = false

    val activeOrder: MutableLiveData<Order?>
        get() = repository.activeOrder


    val newOrder =
        MutableLiveData(Order(passengerId = FirebaseAuth.getInstance().currentUser!!.uid))

    val addresses = MutableLiveData<List<Address>>()

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    fun fetchCurrentAddress() = viewModelScope.launch {

        val coordinates = locationProvider.getLocation()

        if (coordinates != null) {

            val address =
                repository.geocoder.build().requestNameByCoordinates(coordinates.toLatLng).toAddress()
            newOrder.mutation {
                it.value?.addressStart = address
            }
        }
    }

    fun fetchAddresses(value: String) = viewModelScope.launch {

        val list = mutableListOf<Address>()

        withContext(Dispatchers.IO) {
            try {
                val geocodeResults = repository.geocoder.build().requestCoordinatesByName(value).results

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
