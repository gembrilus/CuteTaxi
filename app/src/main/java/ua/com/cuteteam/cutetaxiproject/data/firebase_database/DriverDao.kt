package ua.com.cuteteam.cutetaxiproject.data.firebase_database

import android.util.Log
import com.google.firebase.database.*
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.getValue

class DriverDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("drivers")

    private val newOrdersQuery: Query = ordersRef
        .orderByChild(DbEntries.Orders.Fields.ORDER_STATUS)
        .equalTo(OrderStatus.NEW.name, DbEntries.Orders.Fields.ORDER_STATUS)

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

    @Suppress("UNCHECKED_CAST")
    suspend fun getNewOrders(): List<Order> {
        val ordersReference = newOrdersQuery.ref
        val listOfOrdersSnapshot = ordersReference.getValue().value as List<Order>
        val ordersList = mutableListOf<Order>()

        listOfOrdersSnapshot.forEach {
            ordersList.add(it)
        }
        return ordersList
    }

    fun subscribeForNewOrders(listener: ChildEventListener) {
        val newOrdersRef = newOrdersQuery.ref

        if (!eventListeners.contains(newOrdersRef)) {
            newOrdersRef.addChildEventListener(listener)
        } else {
            removeListeners(newOrdersRef)
            newOrdersRef.addChildEventListener(listener)
        }
    }

    fun writeOrder(order: Order) {
        val orderId = order.orderId
        if (orderId != null) {
            ordersRef.child(orderId).setValue(order).addOnFailureListener {
                Log.e("Firebase: writeOrder()", it.message.toString())
            }.addOnCompleteListener {
                Log.d("Firebase: writeOrder()", "Write is successful")
            }
        } else {
            Log.e("Firebase: writeOrder()", "Order id is null")
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