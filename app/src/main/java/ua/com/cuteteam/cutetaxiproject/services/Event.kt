package ua.com.cuteteam.cutetaxiproject.services

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

sealed class Event {

    class NewOrder(val order: Order): Event()
    class AcceptOrder(val order: Order): Event()
    class ChangedOrder(val order: Order): Event()
    class Near(val coordinates: LatLng): Event()
    object Arrived: Event()

}