package ua.com.cuteteam.cutetaxiproject.api.adapters

import com.squareup.moshi.*
import ua.com.cuteteam.cutetaxiproject.api.directions.Maneuver


/**
 *
 * Moshi adapter class for enum class [Maneuver]
 */
class ManeuverAdapter {

    @ToJson
    fun toJson(maneuver: Maneuver?): String? {
        return maneuver?.maneuver
    }

    @FromJson
    fun fromJson(json: String): Maneuver? {
        return Maneuver.values().find { it.maneuver == json }
    }

}