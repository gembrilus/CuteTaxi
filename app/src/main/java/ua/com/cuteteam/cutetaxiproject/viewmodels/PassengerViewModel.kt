package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ua.com.cuteteam.cutetaxiproject.helpers.PhoneNumberHelper
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.extentions.findBy
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import java.util.*

class PassengerViewModel(
    private val repository: Repository,
    private val context: Context = AppClass.appContext()
) : BaseViewModel(repository) {

    private var dialogShowed = false

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val addressStart: Address = Address()
    val addressFinish: Address = Address()
    var comfortLevel = ComfortLevel.STANDARD

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

/*    fun makeOrder() {
        val order = Order(
            passengerId = userId,
            comfortLevel = comfortLevel,
            addressStart = addressStart,
            addressDestination = addressFinish
        )

        if (order.isReady()) {
            repository.writeOrder(order)
        }
    }*/

    private fun Order.isReady(): Boolean {
        return (this.passengerId != null &&
                this.addressStart?.location != null &&
                this.addressDestination?.location != null)
    }
}
