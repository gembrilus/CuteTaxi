package ua.com.cuteteam.cutetaxiproject.api.directions

import com.squareup.moshi.*

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