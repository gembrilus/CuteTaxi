package ua.com.cuteteam.cutetaxiproject.api.directions

import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*

/**
 *
 * Do request for directions with this class
 *
 */
class DirectionRequest() : APIRequest<DirectionService>() {

    internal constructor(map: Map<String, String>): this(){
        this.map = map
    }

    private lateinit var map: Map<String, String>

    /**
     * Base url for API request. Used by Retrofit
     *
     */
    override val url: String
        get() = BuildConfig.GOOGLE_DIRECTIONS_API_URL

    /**
     *
     * Suspend function. Does request to the server for direction between two locations
     * You need to build DirectionRequest with [DirectionRequest.Builder].
     * It builds [DirectionRequest] with a map of request parameters.
     * Anouther way is to create a DirectionRequest with a map using a
     * secondary constructor DirectionRequest(map: Map<String, String>)
     *
     */
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


        /**
         * Add an origin place to the request parameters
         * @param origin Destination. It can be human readable text or coordinates
         * (by example, "12.23123, 13.121231") or Object ID from Google Maps
         */
        fun addOrigin(origin: String) = apply {
            this@Builder.origin = origin
        }

        /**
         * Add a destination place to the request parameters
         * @param dest Destination. It can be human readable text or coordinates
         * (by example, "12.23123, 13.121231") or Object ID from Google Maps
         */
        fun addDestination(dest: String) = apply {
            destination = dest
        }


        /**
         *  Set <b>true</b> if you need an alternative ways else set <b>false</b>
         */
        fun enableAlternatives() = apply {
            alternatives = true
        }


        /**
         * Add way points if need
         * @param point It can be human readable text or coordinates(by example, "12.23123, 13.121231")
         * or Object ID from Google Maps
         */
        fun addWayPoint(point: String) = apply {
            wayPoints.add(point)
        }


        /**
         * Add an avoid if you need.
         * If you won't to use highways, by example, set value HIGHWAYS
         * @param _avoid Possible values: TOLLS, HIGHWAYS, FERRIES
         */
        fun addAvoid(_avoid: String) = apply {
            avoid.add(_avoid)
        }

        /**
         * Set a language of  response(by example, "ru", "en").
         * @param lang Language in two-words format. By default uses a language of your device
         */
        fun setLanguage(lang: String) = apply {
            language = lang
        }


        /**
         * Set a metric system of response.
         * @param system Metric system. Possible values: METRIC, IMPERIAL. By default uses METRIC.
         */
        fun setMeasureSystem(system: String) = apply {
            units = system
        }


        /**
         * Set a region of response in two-words format. It needs if exist two different objects with the same name
         * @param country Region. Possible values: "ru", "uk"... Not uses by default
         */
        fun setRegion(country: String) = apply {
            region = country
        }


        /**
         * Terminate function that builds a DirectionRequest with full map of parameters
         * After this step you can do asynchronous request by [requestDirection]
         */
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