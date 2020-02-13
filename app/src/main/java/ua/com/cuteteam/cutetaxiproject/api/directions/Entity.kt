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
    val legs: List<LegInfo>
)

data class LegInfo(
    val steps: List<StepInfo>,
    val duration: Map<String, String>,
    val distance: Map<String, String>
)

data class StepInfo(
    @Json(name = "start_location")
    val startLocation: Map<String, Double>,
    @Json(name = "end_location")
    val endLocation: Map<String, Double>,
    val duration: Map<String, String>,
    val distance: Map<String, String>,
    @Json(name = "html_instructions")
    val instructions: String,
    val maneuver: String
)

class Maneuver {
    companion object{
        const val TURN_SLIGHT_LEFT = "turn-slight-left"
        const val TURN_SHARP_LEFT = "turn-sharp-left"
        const val UTURN_LEFT = "uturn-left"
        const val TURN_LEFT = "turn-left"
        const val TURN_SLIGHT_RIGHT = "turn-slight-right"
        const val TURN_SHARP_RIGHT = "turn-sharp-right"
        const val UTURN_RIGHT = "uturn-right"
        const val TURN_RIGHT = "turn-right"
        const val STRAIGHT = "straight"
        const val RAMP_LEFT = "ramp-left"
        const val RAMP_RIGHT = "ramp-right"
        const val MERGE = "merge"
        const val FORK_LEFT = "fork-left"
        const val FORK_RIGHT = "fork-right"
        const val FERRY = "ferry"
        const val FERRY_TRAIN = "ferry-train"
        const val ROUND_ABOUT_LEFT = "roundabout-left"
        const val ROUND_ABOUT_RIGHT = "roundabout-right"
    }
}