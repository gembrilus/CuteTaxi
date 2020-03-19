package ua.com.cuteteam.cutetaxiproject.data.entities

import com.google.android.gms.maps.model.LatLng

data class Coordinates(
    var latitude: Double? = null,
    var longitude: Double? = null
) {
    fun toLatLng(): LatLng? {
        val lat = latitude
        val long = longitude
        return if (lat != null && long != null) {
            LatLng(lat, long)
        } else null
    }
}

