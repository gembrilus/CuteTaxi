package ua.com.cuteteam.cutetaxiproject.data.entities

import com.google.android.gms.maps.model.LatLng

data class Address(
    var location: Coordinates? = null,
    var address: String? = null
)