package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import ua.com.cuteteam.cutetaxiproject.data.entities.Address

/**
 * Class for geocode info. Contains coordinates and names of requested objects
 */
data class Geocode(
    val results: List<Results>,
    val status: String
) {
    fun toLatLng() = results[0].geometry.location.run {
        LatLng(latitude, longitude)
    }

    fun toName() = results[0].formattedAddress
    fun toAddress() = Address(
        location = toCoordinates(),
        address = toName()
    )

    private fun toCoordinates() = results[0].geometry.location.run {
        ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates(latitude, longitude)

    }
}

data class Results(
    @Json(name = "formatted_address")
    val formattedAddress: String,
    val geometry: Geometry
)

data class Geometry(
    val location: LatLng
)
