package ua.com.cuteteam.cutetaxiproject.services

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.ui.main.DriverActivity
import kotlin.coroutines.CoroutineContext

private const val ORDER_ID_NAME = "DriverService_orderId"

class DriverService : BaseService(), CoroutineScope {

    private val orders = mutableListOf<Order>()
    private val removed = mutableListOf<Order>()
    private var orderId: String? = null
    private val fbDao by lazy {
        DriverDao()
    }

    private val locationObserver by lazy {
        Observer<Location> {
            postCoordinates(it)
        }
    }

    private val orderListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            p0.toException().printStackTrace()
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            val order = snapshot.getValue(Order::class.java) ?: return
            notifyOrderCancelled(order)
        }

    }

    private val handler
        get() = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + handler

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        orderId = intent?.getStringExtra(ORDER_ID_NAME)

        launch {

            if (orderId != null) {
//                dao.subscribeForChanges(DbEntries.Orders.TABLE, orderId, orderListener)
                locationLiveData.observeForever(locationObserver)
            }

            fbDao.observeOrders { list ->
                if (orderId == null && list.isNotEmpty()) {
                    val filteredList =
                        list.filter { !orders.contains(it) && !removed.contains(it) }
                    orders.addAll(filteredList)
                    orders.forEach {
                        notifyForNewOrder(it)
                    }
                }
            }
        }


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        locationLiveData.removeObserver(locationObserver)
    }

    private fun postCoordinates(location: Location) {
        val path = "${DbEntries.Orders.TABLE}/${DbEntries.Orders.Fields.DRIVER_LOCATION}"
        fbDao.writeField(DbEntries.Orders.TABLE, location.toLatLng)                             //Change later
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

    private fun notifyOrderCancelled(order: Order){
        notificationUtils.sendNotification(
            title = "ORDER IS CANCELLED!",
            text = "Sorry, but current order was cancelled by client"
        )
        orders.remove(order)
        orderId = null
    }

}