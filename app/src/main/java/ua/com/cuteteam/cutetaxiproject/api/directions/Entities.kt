package ua.com.cuteteam.cutetaxiproject.api.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class with route info
 * @param status server response status, normal - OK
 * @param routes  list of routes. If request will be build with enableAlternatives(true)
 * you can receive more as one route.
 */
@JsonClass(generateAdapter = true)
data class Route(
    val status: String,
    val routes: List<RouteInfo>
)

data class RouteInfo(
    val summary: String,
    val legs: List<LegInfo>,
    @Json(name = "overview_polyline")
    val polyline: Polyline
)

data class LegInfo(
    val steps: List<Step>,
    val duration: Duration,
    val distance: Distance
)

data class Step(
    @Json(name = "start_location")
    val startLocation: Location,
    @Json(name = "end_location")
    val endLocation: Location,
    val duration: Duration,
    val distance: Distance,
    @Json(name = "html_instructions")
    val instructions: String,
    val maneuver: Maneuver?
)

data class Duration(
    val value: Double,
    val text: String
)

data class Distance(
    val value: Double,
    val text: String
)

data class Polyline(
    val points: String
)

data class Location(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lng")
    val longitude: Double
)


/**
 * Maneuvers. You can use it for audio or visual support of navigation
 */
enum class Maneuver(val maneuver: String) {
    TURN_SLIGHT_LEFT("turn-slight-left"),
    TURN_SHARP_LEFT("turn-sharp-left"),
    UTURN_LEFT("uturn-left"),
    TURN_LEFT("turn-left"),
    TURN_SLIGHT_RIGHT("turn-slight-right"),
    TURN_SHARP_RIGHT("turn-sharp-right"),
    UTURN_RIGHT("uturn-right"),
    TURN_RIGHT("turn-right"),
    STRAIGHT("straight"),
    RAMP_LEFT("ramp-left"),
    RAMP_RIGHT("ramp-right"),
    MERGE("merge"),
    FORK_LEFT("fork-left"),
    FORK_RIGHT("fork-right"),
    FERRY("ferry"),
    FERRY_TRAIN("ferry-train"),
    ROUND_ABOUT_LEFT("roundabout-left"),
    ROUND_ABOUT_RIGHT("roundabout-right")
}