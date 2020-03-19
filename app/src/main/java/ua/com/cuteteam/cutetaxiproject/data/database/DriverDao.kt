package ua.com.cuteteam.cutetaxiproject.data.database

import android.util.Log
import com.google.firebase.database.*
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo

class DriverDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("drivers")

    @Suppress("UNCHECKED_CAST")
    fun observeOrders(onSuccess: (List<Order>) -> Unit) {
        val ref = rootRef.child(DbEntries.Orders.TABLE)
            .orderByChild(DbEntries.Orders.Fields.ORDER_STATUS)
            .equalTo(OrderStatus.NEW.name, DbEntries.Orders.Fields.ORDER_STATUS)
            .orderByChild(DbEntries.Orders.Fields.START_ADDRESS)
            .ref
            .child(DbEntries.Orders.Fields.START_ADDRESS)
            .child(DbEntries.Address.LOCATION)
            .startAt(DbEntries.Address.LOCATION)
            .endAt(DbEntries.Address.LOCATION)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("CuteDAO", error.message)
                ref.removeEventListener(this)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(List::class.java) as List<Order>
                onSuccess.invoke(result)
                ref.removeEventListener(this)
            }
        })
    }

    fun subscribeForOrders(driver: Driver, onSuccess: (Order) -> Unit) {
        val ref = rootRef.child(DbEntries.Orders.TABLE)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, prevName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, prevName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, prevName: String?) {
                val newOrder = snapshot.getValue(Order::class.java) as Order
                if (newOrder.addressStart!!.location!!.distanceTo(driver.location!!) <= 10000 &&
                    newOrder.comfortLevel == driver.car?.comfortLevel
                ) {
                    onSuccess.invoke(newOrder)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })

    }

}