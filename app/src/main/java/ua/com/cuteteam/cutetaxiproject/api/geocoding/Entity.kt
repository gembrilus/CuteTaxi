package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.com.cuteteam.cutetaxiproject.api.directions.Location

@JsonClass(generateAdapter = true)
data class Geocode(
    val results: List<Results>,
    val status: String
)

data class Results(
    @Json(name = "formatted_address")
    val formattedAddress: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)