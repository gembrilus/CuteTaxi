package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.com.cuteteam.cutetaxiproject.api.directions.Location

/**
 * Class for geocode info. Contains coordinates and names of requested objects
 */
@JsonClass(generateAdapter = true)
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
    val location: Location
)