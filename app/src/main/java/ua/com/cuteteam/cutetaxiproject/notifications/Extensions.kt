package ua.com.cuteteam.cutetaxiproject.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import ua.com.cuteteam.cutetaxiproject.R


/**
 * Extension-function that creates a notification channel.
 * You can call it from any instance extends Context class.
 * You need to invoke this function when your activity(or fragment or service) starts.
 *
 */
fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.app_name)
        val descriptionText = "Cute Taxi notification channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            packageName,
            name,
            importance
        ).apply {

            description = descriptionText

        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}