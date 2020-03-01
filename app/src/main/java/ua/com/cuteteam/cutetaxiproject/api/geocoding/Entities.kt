package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json

/**
 * Class for geocode info. Contains coordinates and names of requested objects
 */
data class Geocode(
    val results: List<Results>,
    val status: String
){
    fun toLatLng() = results[0].geometry.location.run {
        LatLng(latitude, longitude)
    }

    fun toName() = results[0].formattedAddress

}

data class Results(
    @Json(name = "formatted_address")
    val formattedAddress: String,
    val geometry: Geometry
)

data class Geometry(
    val location: LatLng
)