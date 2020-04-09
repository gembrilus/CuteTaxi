@file: JvmName(name = "Utils")

package ua.com.cuteteam.cutetaxiproject.common

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo

const val speed = 1000 // meter per minute

fun calculatePrice(tax: Double, distance: Double, level: ComfortLevel): Double {
    val koef = when (level) {
        ComfortLevel.STANDARD -> 1.0
        ComfortLevel.COMFORT -> 1.25
        ComfortLevel.ECO -> 1.5
    }
    return tax * distance * koef
}

fun calcDistance(order: Order): Double? {
    val location = order.driverLocation?.latitude?.let { lat ->
        order.driverLocation?.longitude?.let { lon ->
            LatLng(lat, lon)
        }
    }
    val startLat = order.addressStart?.location?.latitude
    val startLon = order.addressStart?.location?.longitude
    if (startLat != null && startLon != null && location != null) {
        return (location distanceTo LatLng(startLat, startLon))
    }
    return null
}

fun prepareDistance(context: Context?, order: Order): String? {
    val d = calcDistance(order) ?: return ""
    return when (d) {
        in 0.0..999.0 -> context?.getString(R.string.units_M, d.toInt().toString())
        else -> context?.getString(R.string.units_KM, (d / 1000).toInt().toString())
    }
}

fun arrivalTime(order: Order): Long? {
    val distance = calcDistance(order)
    return distance?.div(speed)?.toLong() ?: 0
}