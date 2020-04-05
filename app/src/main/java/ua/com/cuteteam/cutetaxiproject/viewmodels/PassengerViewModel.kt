package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import com.google.android.gms.maps.model.Marker
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

class PassengerViewModel(private val repository: Repository) : BaseViewModel(repository) {

    fun createMarkerByCoordinates(latLng: LatLng, tag: Any?, icon: Int) {
        mapAction.value = MapAction.CreateMarkerByCoordinates(latLng, tag, icon)
    }

    fun createOrUpdateMarker(
        tag: Any?,
        icon: Int,
        callback: ((Marker?) -> Unit)? = null
    ) {
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
