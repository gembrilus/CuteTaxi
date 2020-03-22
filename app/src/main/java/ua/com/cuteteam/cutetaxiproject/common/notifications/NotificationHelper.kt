package ua.com.cuteteam.cutetaxiproject.common.notifications

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ua.com.cuteteam.cutetaxiproject.R

private const val UPDATABLE_ID = 0
private var _NOTE_ID = 1
private val NOTE_ID = _NOTE_ID++
private const val GROUP_KEY = "ua.com.cuteteam.cutetaxiproject.CuteTeamGroup"


/**
 * Helps to build and send notifications
 * @param context Context required
 *
 */
class NotificationUtils(private val context: Context) {

    private var NOTIFICATION_CHANNEL_ID = context.packageName

    private val extras: Bundle? = null
    private var actions = mutableListOf<NotificationCompat.Action>()
    private var intent: PendingIntent? = null

    /**
     * Builder-function. Adds an action into a notification
     * @param action is an action created by [NotificationCompat.Action]
     * @return [NotificationUtils]
     */
    fun addAction(action: NotificationCompat.Action) = apply {
        actions.add(action)
    }


    /**
     * Builder-function. Adds an action into a notification
     * @param icon An icon ID from resources
     * @param name A name of the action. Will be shown near the icon
     * @param pendingIntent An action wrapped in PendingIntent.
     * @return [NotificationUtils]
     */
    fun addAction(icon: Int, name: String, pendingIntent: PendingIntent) = apply {
        val action = NotificationCompat.Action(icon, name, pendingIntent)
        actions.add(action)
    }


    /**
     * Builder-function. Adds extra data into a notification
     * @param data Extra data wrapped in [Bundle]
     * @return [NotificationUtils]
     */
    fun addExtraData(data: Bundle) = apply {
        extras?.putAll(data)
    }

    /**
     * Builder-function. Allows you to set an intent for starting an activity
     * by click on the notification
     * @param clazz An activity class. Example:  YourActivity::class
     * @return [NotificationUtils]
     *
     */
    fun setStartActivityIntent(clazz: Class<out Activity>) = apply {
        intent = Intent(context, clazz)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            .run {
                PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
            }
    }


    /**
     * It sends a notification.
     * For setting up your notification use builder-functions of this class.
     * @param title You can use only it if you need the simplest notification
     * @param bigText Use it if you need show more info. You receive an expandable notification
     *
     */
    fun sendNotification(
        title: String,
        text: String,
        bigText: String? = null,
        updatable: Boolean = false
    ) = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .apply {

            setSmallIcon(R.drawable.cute_taxi_headpiece)
            setContentTitle(title)
            setContentText(text)
            priority = NotificationCompat.PRIORITY_DEFAULT
            bigText?.let { setStyle(NotificationCompat.BigTextStyle().bigText(it)) }
            intent?.let { setContentIntent(it) }
            actions.forEach { addAction(it) }
            extras?.let { addExtras(it) }
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setAutoCancel(true)
            setWhen(System.currentTimeMillis())
            setDefaults(Notification.DEFAULT_ALL)
            setGroup(GROUP_KEY)
        }
        .build()
        .run {
            val id = if (updatable) UPDATABLE_ID else NOTE_ID
            NotificationManagerCompat.from(context)
                .notify(id, this)
        }


    /**
     * Cancel all previously shown notifications.
     *
     */
    fun cancelAll() = NotificationManagerCompat.from(context).cancelAll()

}