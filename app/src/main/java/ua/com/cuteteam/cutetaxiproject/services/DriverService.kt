package ua.com.cuteteam.cutetaxiproject.services

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.activities.DriverActivity
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng

class DriverService : BaseService(), CoroutineScope {

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
        orderId = intent
            ?.getStringExtra(ORDER_ID_NAME)
            ?: appSettingsHelper.activeOrderId

        orderId
            ?.let {
                fbDao.subscribeForChanges(DbEntries.Orders.TABLE, it, orderListener)
                locationLiveData.observeForever(locationObserver)
            }
            ?: fbDao.observeOrders { list ->
                launch(Dispatchers.Main) {
                    val currentLocation = locationProvider.getLocation()?.toLatLng
                    if (list.isNotEmpty()) {
                        val newOrders =
                            list.filter { it.comfortLevel == appSettingsHelper.carClass }
                                .filter {
                                    val lat = it.addressStart?.location?.latitude
                                    val lon = it.addressStart?.location?.longitude
                                    if (lat != null && lon != null && currentLocation != null) {
                                        val latLng = LatLng(lat, lon)
                                        val distance = currentLocation distanceTo latLng
                                        distance <= 5000
                                    } else false
                                }
                        newOrders.forEach(::notifyForNewOrder)
                    }
                }
            }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationLiveData.removeObserver(locationObserver)
        fbDao.removeAllListeners()
    }

    private fun onOrderCancel(order: Order) {
        if (order.orderStatus == OrderStatus.CANCELLED) {
            notifyOrderCancelled()
        }
    }

    private fun postCoordinates(location: Location) {
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
            .setStartActivityIntent(DriverActivity::class.java)
            .sendNotification(
                "New order!",
                "$from - $to, $price hrn",
                "From: $from\n" +
                        "To: $to\n" +
                        "Distance: $distance\n" +
                        "Price: $price"
            )
    }

    private fun notifyOrderCancelled() {
        notificationUtils.sendNotification(
            title = "ORDER IS CANCELLED!",
            text = "Sorry, but current order was cancelled by client"
        )
        cancelOrder()
    }

    private fun cancelOrder() {
        locationLiveData.removeObserver(locationObserver)
        orderId?.let {
            FirebaseDatabase.getInstance().reference.child(DbEntries.Orders.TABLE).child(
                it
            ).removeEventListener(orderListener)
        }
        orderId = null
        appSettingsHelper.activeOrderId = null
    }

}