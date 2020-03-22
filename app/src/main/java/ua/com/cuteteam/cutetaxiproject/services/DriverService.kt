package ua.com.cuteteam.cutetaxiproject.services

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.activities.DriverActivity

private const val ORDER_ID_NAME = "DriverService_orderId"

class DriverService : BaseService(), CoroutineScope {

    private val orders = mutableListOf<Order>()
    private var orderId: String? = null
    private val fbDao by lazy {
        DriverDao()
    }

    private val locationObserver by lazy {
        Observer<Location> {
            postCoordinates(it)
        }
    }

    private val orderListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.toException().printStackTrace()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Order::class.java)?.let { onOrderCancel(it) }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        launch {
            orderId = intent?.getStringExtra(ORDER_ID_NAME) ?: appSettingsHelper.activeOrderId
            val location = locationProvider.getLocation()?.toLatLng

            if (orderId != null) {
//                dao.subscribeForChanges(DbEntries.Orders.TABLE, orderId, orderListener)               //TODO:Uncomment when a method FbDao appears
                locationLiveData.observeForever(locationObserver)
            }

            fbDao.observeOrders { list ->
                if (orderId == null && list.isNotEmpty()) {
                    val filteredList =
                        list
                            .filter { it.comfortLevel == appSettingsHelper.carClass }
                            .filter {
                                val startLocation = it.addressStart?.location.run {
                                    this?.latitude?.let { lat ->
                                        longitude?.let { lon ->
                                            LatLng(lat, lon)
                                        }
                                    }
                                }
                                if (location != null && startLocation != null) {
                                    (location distanceTo startLocation) < 5000.0
                                } else false
                            }
                            .filter { !orders.contains(it) }
                    orders.clear()
                    orders.addAll(filteredList)
                    orders.forEach(this@DriverService::notifyForNewOrder)
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationLiveData.removeObserver(locationObserver)
    }

    private fun onOrderCancel(order: Order) {
        if (order.orderStatus == OrderStatus.CANCELLED) {
            notifyOrderCancelled(order)
        }
    }

    private fun postCoordinates(location: Location) {
        val path = "${DbEntries.Orders.TABLE}/${DbEntries.Orders.Fields.DRIVER_LOCATION}"
        orderId?.let {
            fbDao.updateOrder(
                it,
                DbEntries.Orders.Fields.DRIVER_LOCATION,
                location.toLatLng
            )
        }
    }

    private fun getAcceptOrderIntent(order: Order): NotificationCompat.Action {
        val intent = Intent(applicationContext, DriverActivity::class.java).apply {
            putExtra(ACCEPTED_ORDER_ID, order.orderId)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        return NotificationCompat.Action(
            R.drawable.ct_accept,
            applicationContext.getString(R.string.action_name_accept),
            pendingIntent
        )
    }

    private fun notifyForNewOrder(order: Order) {
        val from = order.addressStart?.address
        val to = order.addressDestination?.address
        val distance = order.distance
        val price = order.price

        notificationUtils
            .addAction(getAcceptOrderIntent(order))
            .sendNotification(
                "New order!",
                "From: $from\n" +
                        "To: $to\n" +
                        "Distance: $distance\n" +
                        "Price: $price"
            )
    }

    private fun notifyOrderCancelled(order: Order) {
        notificationUtils.sendNotification(
            title = "ORDER IS CANCELLED!",
            text = "Sorry, but current order was cancelled by client"
        )
        orders.remove(order)
        orderId = null
        appSettingsHelper.activeOrderId = null
    }

}