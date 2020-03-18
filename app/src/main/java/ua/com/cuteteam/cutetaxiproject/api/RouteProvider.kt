package ua.com.cuteteam.cutetaxiproject.api

import com.google.android.gms.maps.model.LatLng
import io.leonard.PolylineUtils
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider.RouteSummary
import ua.com.cuteteam.cutetaxiproject.api.directions.*
import ua.com.cuteteam.cutetaxiproject.api.roads.RoadsRequest
import java.util.*
import kotlin.math.min

/**
 * Class provides an info about routes. Class has a private constructor
 * Use RouteProvider.Builder() for building routes
 * @see DirectionRequest
 * @see RoadsRequest
 * @see RouteSummary
 *
 */
class RouteProvider private constructor(
    private val directionRequest: DirectionRequest,
    private val roadsRequest: RoadsRequest
) {

    private lateinit var map: Map<String, String>
    private var fastest = false
    private var shortest = false


    /**
     * Terminal suspend function that return a list with summary info about route or routes
     * @return List<RouteSummary> contains list of routes, their total distance, duration, maneuvers
     * @see RouteSummary
     */
    suspend fun routes(): List<RouteSummary> {

        val list = mutableListOf<RouteSummary>()

        if (fastest) {
            allRoutes().minBy { it.time }?.let { list.add(it) }
        }
        if (shortest) {
            allRoutes().minBy { it.distance }?.let { list.add(it) }
        }
        if (!fastest && !shortest) {
            return allRoutes()
        }
        return list

    }


    private suspend fun allRoutes(): List<RouteSummary> {
        val list = mutableListOf<RouteSummary>()
        directionRequest.requestDirection(map)
            .routes.forEach { routeInfo ->
            val polyline = decodedPolyline(routeInfo)
            val points = snapToRoads(polyline)
            val summary = createRouteSummary(routeInfo, points.toTypedArray())
            list.add(summary)
        }
        return list
    }

    /**
     * This function returns exclusive points where maneuvers are happened
     * @param routeInfo Routeinfo is an info about one route from any in the instance [Route]
     */
    fun getManeuverPoints(routeInfo: RouteInfo) =
        routeInfo.legs
            .flatMap { leg -> leg.steps }
            .map { step ->
                listOf(
                    LatLng(
                        step.startLocation.latitude,
                        step.startLocation.longitude
                    ),
                    LatLng(
                        step.endLocation.latitude,
                        step.endLocation.longitude
                    )
                )
            }
            .flatten()
            .distinct()


    private fun decodedPolyline(routeInfo: RouteInfo) =
        PolylineUtils.decode(routeInfo.polyline.points, 5)
            .map { LatLng(it.latitude, it.longitude) }

    private suspend fun snapToRoads(points: List<LatLng>): List<LatLng> {

        val snappedPoints = mutableListOf<LatLng>()

        var offset = 0
        while (offset < points.size) {
            if (offset > 0) {
                offset -= 5
            }

            val lowerBound = offset
            val upperBound = min(offset + 100, points.size)

            val path = points
                .subList(lowerBound, upperBound)

            val roadsPoints = roadsRequest.getRoads(path)
                .snappedPoints

            var passedOverlap = false

            roadsPoints.forEach {

                if (offset == 0 || (it.originalIndex != null && it.originalIndex >= 5)) {
                    passedOverlap = true
                }

                if (passedOverlap) {
                    snappedPoints.add(it.location)
                }

            }
            offset = upperBound
        }

        return snappedPoints
    }


    private fun createRouteSummary(routeInfo: RouteInfo, polyline: Array<LatLng>) =
        RouteSummary(
            getParam(routeInfo) { distance.value },
            getParam(routeInfo) { duration.value },
            polyline,
            getStepsParams(routeInfo) { maneuver },
            getStepsParams(routeInfo) { instructions }
        )


    private inline fun <reified T> getStepsParams(
        routeInfo: RouteInfo,
        getParam: Step.() -> T
    ): List<T> =
        routeInfo.legs
            .flatMap { leg -> leg.steps }
            .map { getParam.invoke(it) }


    private fun getParam(routeInfo: RouteInfo, supplier: LegInfo.() -> Double?): Double =
        routeInfo.legs
            .mapNotNull { leg ->
                supplier.invoke(leg)
            }
            .sumByDouble { it }


    class Builder(
        private val directionRequest: DirectionRequest = DirectionRequest(),
        private val roadsRequest: RoadsRequest = RoadsRequest()
    ) {
        private lateinit var origin: String
        private lateinit var destination: String
        private var wayPoints: MutableList<String> = mutableListOf()
        private var alternatives: Boolean = false
        private var avoid: MutableSet<String> = mutableSetOf()
        private var language = Locale.getDefault().language
        private var units = RequestParameters.Units.METRIC
        private var region: String = ""
        private var fastest = false
        private var shortest = false


        /**
         * Add an origin place to the request parameters
         * @param origin is a origin place. It can be human readable text or coordinates
         * (by example, "12.23123, 13.121231") or Object ID from Google Maps
         */
        fun addOrigin(origin: String) = apply {
            this@Builder.origin = origin
        }

        /**
         * Add an origin place to the request parameters
         * @param origin is a origin place coordinates in [LatLng]
         */
        fun addOrigin(origin: LatLng) =
            addOrigin("${origin.latitude},${origin.longitude}")


        /**
         * Add a destination place to the request parameters
         * @param dest is a destination place. It can be human readable text or coordinates
         * (by example, "12.23123, 13.121231") or Object ID from Google Maps
         */
        fun addDestination(dest: String) = apply {
            destination = dest
        }


        /**
         * Add a destination place to the request parameters
         * @param dest is a destination place coordinates in [LatLng]
         */
        fun addDestination(dest: LatLng) =
            addDestination("${dest.latitude},${dest.longitude}")


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
         * Add way points if need
         * @param point in [LatLng] format
         */
        fun addWayPoint(point: LatLng) = apply {
            wayPoints.add("${point.latitude},${point.longitude}")
        }


        /**
         * Add an avoid if you need.
         * If you won't to use highways, by example, set value HIGHWAYS
         * @param _avoid Possible values: [RequestParameters.AVOID.TOLLS], [RequestParameters.AVOID.HIGHWAYS], [RequestParameters.AVOID.FERRIES]
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
         * Builder-function - Search for the fastest route
         * @return RouteBuilder
         */
        fun findTheFastest() = apply {
            fastest = true
        }


        /**
         * Builder-function - Search for the shortest route
         * @return RouteBuilder
         */
        fun findTheShortest() = apply {
            shortest = true
        }


        /**
         * Terminate function that builds a DirectionRequest with full map of parameters
         * After this step you can do asynchronous request by [routes]
         */
        fun build(): RouteProvider {
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

            if (region.isNotEmpty()) {
                map[RequestParameters.REGION] = region
            }

            return RouteProvider(directionRequest, roadsRequest).apply {
                this.map = map
                this.fastest = this@Builder.fastest
                this.shortest = this@Builder.shortest
            }
        }
    }

    inner class RouteSummary(
        val distance: Double = 0.0,
        val time: Double = 0.0,
        val polyline: Array<LatLng>,
        val maneuvers: List<Maneuver?>,
        val instructions: List<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RouteSummary

            if (distance != other.distance) return false
            if (time != other.time) return false
            if (!polyline.contentEquals(other.polyline)) return false
            if (maneuvers != other.maneuvers) return false
            if (instructions != other.instructions) return false

            return true
        }

        override fun hashCode(): Int {
            var result = distance.hashCode()
            result = 31 * result + time.hashCode()
            result = 31 * result + polyline.contentHashCode()
            result = 31 * result + maneuvers.hashCode()
            result = 31 * result + instructions.hashCode()
            return result
        }
    }

}