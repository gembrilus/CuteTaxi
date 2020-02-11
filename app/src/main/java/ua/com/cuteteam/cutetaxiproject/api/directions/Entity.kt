package ua.com.cuteteam.cutetaxiproject.api.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Route(
    val status: String,
    val routes: List<RouteInfo>
)

data class RouteInfo(
    val summary: String,
    val legs: List<Steps>,
    val duration: Map<String, String>,
    val distance: Map<String, String>
)

data class Steps(
    val steps: List<StepInfo>
)

data class StepInfo(
    @Json(name = "start_location")
    val startLocation: Map<String, Double>,
    @Json(name = "end_location")
    val endLocation: Map<String, Double>,
    val duration: Map<String, String>,
    val distance: Map<String, String>
)