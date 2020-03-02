package ua.com.cuteteam.cutetaxiproject.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ua.com.cuteteam.cutetaxiproject.activities.MainActivity
import ua.com.cuteteam.cutetaxiproject.common.notifications.NotificationUtils
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus

abstract class BaseService : Service() {

    private val db = FirebaseDatabase.getInstance()
    private val listeners = mutableListOf<ValueEventListener>()

    private fun createOrderListener(block: (Order?) -> Unit): ValueEventListener {

        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.toException().printStackTrace()
            }

            override fun onDataChange(p0: DataSnapshot) {
                block.invoke(p0.getValue(Order::class.java))
            }
        }

        listeners.add(listener)
        return listener
    }

    private val notificationUtils by lazy { NotificationUtils(applicationContext) }

        protected fun subscribeToOrder(orderId: String, block: (Order?) -> Unit) {
            db.getReference("orders").child(orderId).addValueEventListener(
                createOrderListener {
                    block.invoke(it)
                }
            )
        }

    protected fun sendNotification(event: Event) {

        fun notify(title: String, text: String) =
            notificationUtils.sendNotification(title, text)

        when (event) {
            is Event.NewOrder -> {

                val from = event.order.addressStart?.address
                val to = event.order.addressDestination?.address
                val distance = event.order.distance
                val price = event.order.price

                notificationUtils
                    .addAction(NotificationUtils.acceptOrderIntent(applicationContext))
                    .addAction(NotificationUtils.declineOrderIntent(applicationContext))
                    .sendNotification(
                        "You have a new order!",
                        "From: $from\n" +
                                "To: $to\n" +
                                "Distance: $distance\n" +
                                "Price: $price"
                    )
            }
            is Event.AcceptOrder -> {

                val time = "5 min"                          // Change later
                val car = "тачка на прокачку"               // Change later

                notificationUtils
                    .setStartActivityIntent(MainActivity::class.java)    // Change later
                    .sendNotification(
                        "Your order is accepted!",
                        "In $time expect $car"
                    )

            }
            is Event.ChangedOrder -> {

                val orderStatus = event.order.orderStatus
                if (orderStatus == OrderStatus.CANCEL) {
                    notify(
                        "Order was changed!",
                        "You order is canceled"
                    )
                }
            }
            is Event.Near -> {

                notify(
                    "Changes for your order!",
                    "A car will be in 1 min"
                )

            }
            is Event.Arrived -> {

                notify(
                    "Changes for your order!",
                    "A car is already awaiting for you"
                )

            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}