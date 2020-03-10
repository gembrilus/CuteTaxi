package ua.com.cuteteam.cutetaxiproject.extentions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.lang.Math.pow
import kotlin.math.*

infix fun LatLng.distanceTo(latLng: LatLng): Double {

    val lat1 = latitude * PI / 180
    val lat2 = latLng.latitude * PI / 180
    val lon1 = longitude * PI / 180
    val lon2 = latLng.longitude * PI / 180


    val earthRadius = 6372795.0
    val delta = lon2 - lon1
    val cosL1 = cos(lat1)
    val cosL2 = cos(lat2)
    val sinL1 = sin(lat1)
    val sinL2 = sin(lat2)
    val cosDelta = cos(delta)
    val sinDelta = sin(delta)

    val y = sqrt((cosL2 * sinDelta).pow(2) + (cosL1 * sinL2 - sinL1 * cosL2 * cosDelta).pow(2))
    val x = sinL1 * sinL2 + cosL1 * cosL2 * cosDelta

    return earthRadius * atan2(y, x)
}

val Location.toLatLng: LatLng get() = LatLng(latitude, longitude)