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

    fun observeOrders(onSuccess: (List<Order>) -> Unit) {

        val ref = rootRef.child(DbEntries.Orders.TABLE)
            .orderByChild(DbEntries.Orders.Fields.ORDER_STATUS)
            .equalTo(OrderStatus.NEW.name)

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                val result = p0.children
                    .mapNotNull {
                        it.getValue(Order::class.java)
                    }
                    .toList()
                onSuccess.invoke(result)
            }
        }
        if (!eventListeners.contains(ref.ref)) {
            ref.addValueEventListener(listener)
            eventListeners[ref.ref] = listener
        }
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
                if (newOrder.addressStart!!.location!!.toLatLng()!!
                        .distanceTo(driver.location!!.toLatLng()!!) <= 10000 &&
                    newOrder.comfortLevel == driver.car?.carClass
                ) {
                    onSuccess.invoke(newOrder)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    fun writeOrder(order: Order) {
        order.orderId?.let {
            ordersRef.child(it).setValue(order).addOnFailureListener {
                Log.e("Firebase: writeOrder()", it.message.toString())
            }.addOnCompleteListener {
                Log.d("Firebase: writeOrder()", "Write is successful")
            }
        }
    }

    fun updateOrder(id: String, field: String, value: Any) {
        ordersRef.child(id).child(field).setValue(value).addOnFailureListener {
            Log.e("Firebase: updateOrder()", it.message.toString())
        }.addOnCompleteListener {
            Log.d("Firebase: updateOrder()", "Write is successful")
        }
    }


}