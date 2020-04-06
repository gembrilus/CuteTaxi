package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.MarkerData
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

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

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val addressStart: Address = Address()
    val addressFinish: Address = Address()
    var comfortLevel = ComfortLevel.STANDARD

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    private fun Order.isReady(): Boolean {
        return (this.passengerId != null &&
                this.addressStart?.location != null &&
                this.addressDestination?.location != null)
    }
}
