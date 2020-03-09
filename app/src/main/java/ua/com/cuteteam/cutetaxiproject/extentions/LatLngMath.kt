package ua.com.cuteteam.cutetaxiproject.extentions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


operator fun LatLng.times(number: Int) =
    LatLng(latitude * number, longitude * number)

operator fun LatLng.times(number: Double) =
    LatLng(latitude * number, longitude * number)

operator fun Int.times(latLng: LatLng) =
    LatLng(latLng.latitude * this, latLng.longitude * this)

operator fun Double.times(latLng: LatLng) =
    LatLng(latLng.latitude * this, latLng.longitude * this)

operator fun LatLng.times(latLng: LatLng) =
    LatLng(latitude * latLng.latitude, longitude * latLng.longitude)

operator fun LatLng.plus(latLng: LatLng) =
    LatLng(latitude + latLng.latitude, longitude + latLng.longitude)


operator fun LatLng.minus(latLng: LatLng) =
    LatLng(latitude - latLng.latitude, longitude - latLng.longitude)

operator fun LatLng.div(number: Int) =
    LatLng(latitude / number, longitude / number)

operator fun LatLng.dec() = LatLng(latitude - 1, longitude - 1)
operator fun LatLng.inc() = LatLng(latitude + 1, longitude + 1)

fun sin(latLng: LatLng) = LatLng(sin(latLng.latitude), sin(latLng.longitude))

infix fun LatLng.distanceTo(latLng: LatLng): Double {
    val earthRadius = 6371000
    val delta = latLng - this

    val gaverSinus = sin(delta / 2) * sin(delta / 2)

    return 2 * earthRadius * asin(
        sqrt(
            gaverSinus.latitude + cos(latitude) * cos(latLng.latitude) * gaverSinus.longitude
        )
    )
}

fun LatLng.arrivalTimeTo(to: LatLng, speed: Float = 1000f) = (this distanceTo to)/speed

val Location.toLatLng: LatLng get() = LatLng(latitude, longitude)