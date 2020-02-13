package ua.com.cuteteam.cutetaxiproject.api.directions

import com.google.android.gms.maps.model.LatLng

private const val DISTANCE_VALUE = "value"
private const val DURATION_VALUE = "value"
private const val LATITUDE = "lat"
private const val LONGITUDE = "lng"


/**
 * Class provides an info about routes
 * @param route is an instance of Route received in [DirectionRequest]
 * @see DirectionRequest
 * @see Route
 *
 */
class RouteProvider(private val route: Route) {

    private var fastest = false
    private var shortest = false

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
     * Terminal method that return a list with summary info about route or routes
     * @return List<Summary> contains list of routes, their total distance and duration
     * @see Summary
     */
    fun build(): List<Summary> {

        val list = mutableListOf<Summary>()

        if (fastest){
            buildAll().minBy { it.time }?.let { list.add(it) }
        }
        if (shortest){
            buildAll().minBy { it.distance }?.let { list.add(it) }
        }
        if (!fastest && !shortest){
            return buildAll()
        }
        return list

    }


    /**
     * Terminal method that return a list with polilines only
     * @return List<Array<LatLng>> contains list of polylines
     *
     */
    fun buildPolylines(): List<Array<LatLng>> {

        val list = mutableListOf<Array<LatLng>>()

        if (fastest){
            buildAll().minBy { it.time }?.let { list.add(it.polyline) }
        }
        if (shortest){
            buildAll().minBy { it.distance }?.let { list.add(it.polyline) }
        }
        if (!fastest && !shortest){
            return buildAll().map { it.polyline }
        }
        return list
    }


    private fun buildAll() = mutableListOf<Summary>().apply {
        route.routes.forEach { route ->
            add(
                Summary(
                    getParam(route){ distance[DISTANCE_VALUE]?.toDouble() },
                    getParam(route){ duration[DURATION_VALUE]?.toDouble() },
                    getPolyline(route),
                    getStepsParams(route){ maneuver },
                    getStepsParams(route){ instructions }
                )
            )
        }
    }


    private fun getPolyline(routeInfo: RouteInfo) =
        routeInfo.legs
            .flatMap { leg -> leg.steps }
            .map { step ->

                val start = LatLng(
                    step.startLocation.getValue(LATITUDE),
                    step.startLocation.getValue(LONGITUDE)
                )
                val end = LatLng(
                    step.endLocation.getValue(LATITUDE),
                    step.endLocation.getValue(LONGITUDE)
                )

                listOf(start, end)
            }
            .flatten()
            .toTypedArray()


    private fun getStepsParams(routeInfo: RouteInfo, getParam: StepInfo.() -> String): List<String> =
        routeInfo.legs
            .flatMap { leg -> leg.steps }
            .map { getParam.invoke(it) }


    private fun getParam(routeInfo: RouteInfo, supplier: LegInfo.() -> Double?): Double =
        routeInfo.legs
            .mapNotNull { leg ->
                supplier.invoke(leg)
            }
            .sumByDouble { it }


    inner class Summary(
        val distance: Double = 0.0,
        val time: Double = 0.0,
        val polyline: Array<LatLng> = arrayOf(),
        val maneuvers: List<String> = arrayListOf(),
        val instructions: List<String> = arrayListOf()
    )

}