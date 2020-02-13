package ua.com.cuteteam.cutetaxiproject.api.directions

import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*

class DirectionRequest() : APIRequest<DirectionService>() {

    internal constructor(map: Map<String, String>): this(){
        this.map = map
    }

    private lateinit var map: Map<String, String>

    override val url: String
        get() = BuildConfig.GOOGLE_DIRECTIONS_API_URL

    suspend fun requestDirection() =
        getService<DirectionService>().getDirection(map)

    class Builder {
        private lateinit var origin: String
        private lateinit var destination: String
        private var wayPoints: MutableList<String> = mutableListOf()
        private var alternatives: Boolean = false
        private var avoid: MutableSet<String> = mutableSetOf()
        private var language = Locale.getDefault().language
        private var units = RequestParameters.Units.METRIC
        private var region: String = ""


        fun addOrigin(origin: String) = apply {
            this@Builder.origin = origin
        }

        fun addDestination(dest: String) = apply {
            destination = dest
        }

        fun enableAlternatives() = apply {
            alternatives = true
        }

        fun addWayPoint(point: String) = apply {
            wayPoints.add(point)
        }

        fun addAvoid(_avoid: String) = apply {
            avoid.add(_avoid)
        }

        fun setLanguage(lang: String) = apply {
            language = lang
        }

        fun setMeasureSystem(system: String) = apply {
            units = system
        }

        fun setRegion(country: String) = apply {
            region = country
        }

        fun build(): DirectionRequest {
            val map = mutableMapOf<String, String>()

            map[RequestParameters.ORIGIN_PLACE] = origin
            map[RequestParameters.DESTINATION_PLACE] = destination

            if (wayPoints.isNotEmpty()) {
                map[RequestParameters.WAY_POINTS] = wayPoints.joinToString("|", "", "")
            }

            if (alternatives) {
                map[RequestParameters.IS_ALTERNATIVE_WAYS] = alternatives.toString()
            }

            if (avoid.isNotEmpty()) {
                map[RequestParameters.AVOID] = avoid.joinToString("|", "", "")
            }

            map[RequestParameters.LANG] = language
            map[RequestParameters.UNITS] = units

            if (region.isNotEmpty()){
                map[RequestParameters.REGION] = region
            }

            return DirectionRequest(map)
        }
    }
}