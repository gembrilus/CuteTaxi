package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.providers.LocationProvider
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
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import java.util.*

class PassengerViewModel(
    private val repository: Repository,
    private val context: Context = AppClass.appContext()
) : BaseViewModel(repository) {

    fun createOrUpdateMarker(tag: Any?,
                             icon: Int,
                             callback: ((Marker?) -> Unit)? = null) {
        mapAction.value = MapAction.StartMarkerUpdate(tag, icon, callback)
    }

    fun stopMarkerUpdate() {
        mapAction.value = MapAction.StopMarkerUpdate()
    }

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val addressStart: Address = Address()
    val addressFinish: Address = Address()
    var comfortLevel = ComfortLevel.STANDARD

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

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
