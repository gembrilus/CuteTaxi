package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import ua.com.cuteteam.cutetaxiproject.extentions.getValue

class PassengerDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("passengers")

    override suspend fun getUser(uid: String): Passenger? {
        val userData = usersRef.child(uid).getValue()
        return userData.getValue(Passenger::class.java)
    }

    fun writeOrder(order: Order?): DatabaseReference {
        val ref = ordersRef.push()
        order?.let {
            it.orderId = ref.key
            ref.setValue(it)
        }
        return ref
    }
}