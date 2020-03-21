package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

class PassengerDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("passengers")

    fun writeOrder(order: Order?): DatabaseReference {
        val ref = rootRef.child(DbEntries.Orders.TABLE).push()
        order?.let {
            ref.setValue(it)
        }
        return ref
    }
}