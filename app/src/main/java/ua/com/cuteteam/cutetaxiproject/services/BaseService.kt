package ua.com.cuteteam.cutetaxiproject.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.notifications.NotificationUtils
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

const val ACCEPTED_ORDER_ID = "AcceptedOrderId"

abstract class BaseService : Service() {

    protected val notificationUtils by lazy {
        NotificationUtils(applicationContext)
    }
    protected val appSettingsHelper by lazy {
        AppSettingsHelper(applicationContext)
    }

    protected val locationLiveData by lazy {
        LocationLiveData()
    }

    private val dialerIntent =
        PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${appSettingsHelper.phone}")
            },
            0
        ).run {
            NotificationCompat.Action(
                R.drawable.ct_call_phone,
                applicationContext.getString(R.string.action_name_call),
                this)
        }

/*    protected fun sendNotification(event: Event) {

        fun notify(title: String, text: String) =
            notificationUtils.sendNotification(title, text)

        when (event) {
            is Event.AcceptOrder -> {

                val time = event.order.arrivingTime
                val car = "тачка на прокачку"               // Change later

                notificationUtils
                    .setStartActivityIntent(PassengerActivity::class.java)
                    .sendNotification(
                        "Your order is accepted!",
                        "In $time expect $car"
                    )

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
    }*/

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}