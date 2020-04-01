package ua.com.cuteteam.cutetaxiproject.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng

class DriverRepository : Repository() {
    val dao = DriverDao()

    val orders = MutableLiveData<MutableList<Order>>()

    fun fetchNewOrders() = ioScope.launch {
        val newOrders = dao.getNewOrders()
        withContext(Dispatchers.Main) {
            orders.value?.addAll(newOrders)
        }
    }

    fun subscribeForNewOrders() {
        dao.subscribeForNewOrders(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("subscribeForNewOrders()", error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val orderStatus = snapshot
                    .child(DbEntries.Orders.Fields.ORDER_STATUS)
                    .value as OrderStatus

                if (orderStatus != OrderStatus.NEW) {
                    val order: Order = snapshot.value as Order
                    orders.value?.remove(order)
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                if (isPassengerAccessible(snapshot)) {
                    val order: Order = snapshot.value as Order
                    orders.value?.add(order)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val order: Order = snapshot.value as Order
                orders.value?.remove(order)
            }
        })
    }

    fun isPassengerAccessible(snapshot: DataSnapshot): Boolean {

        val startLocation = snapshot
            .child(DbEntries.Orders.Fields.START_ADDRESS)
            .child(DbEntries.Address.LOCATION)
            .value as Coordinates

        val driverLocation = observableLocation.value
        val clientLatLng = startLocation.toLatLng()

        return (driverLocation != null && clientLatLng != null) &&
                (clientLatLng.distanceTo(driverLocation.toLatLng) <= 5000)
    }
}
