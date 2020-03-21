package ua.com.cuteteam.cutetaxiproject.viewmodels

import com.google.firebase.auth.FirebaseAuth
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.LocationProvider
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import ua.com.cuteteam.cutetaxiproject.ui.main.models.BaseViewModel

class PassengerViewModel(private val repository: Repository) : BaseViewModel(repository) {

    private var dialogShowed = false

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val addressStart: Address = Address()
    val addressFinish: Address = Address()
    var comfortLevel = ComfortLevel.STANDARD

    val observableLocation: LocationLiveData
        get() = repository.observableLocation

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || repository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
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
