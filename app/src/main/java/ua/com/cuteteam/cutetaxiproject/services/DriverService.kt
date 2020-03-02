package ua.com.cuteteam.cutetaxiproject.services

import android.content.Intent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.database.DriverEntry
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver

class DriverService : BaseService() {

    private val dao by lazy { DriverDao() }

    private var listener: ValueEventListener? = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            p0.toException().printStackTrace()
        }

        override fun onDataChange(p0: DataSnapshot) {
            val driver = p0.getValue(Driver::class.java)
            val orderId = driver?.orderId
            if (orderId != null) {
                subscribeToOrder(orderId) {

                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        dao.subscribeForChanges(DriverEntry.)

        return START_STICKY
    }

}