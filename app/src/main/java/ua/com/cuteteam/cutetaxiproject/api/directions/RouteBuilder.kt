package ua.com.cuteteam.cutetaxiproject.api.directions

import com.google.android.gms.maps.model.LatLng

private const val DISTANCE_VALUE = "value"
private const val DURATION_VALUE = "value"
private const val LATITUDE = "lat"
private const val LONGITUDE = "lng"

class RouteBuilder(private val route: Route) {

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
     *
     * class Summary(
     * val distance: Double,
     * val time: Double,
     * val polyline: Array<LatLng>
     * )
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
                    getDistance(route),
                    getDuration(route),
                    getPolyline(route)
                )
            )
        }
    }

    private fun getDistance(routeInfo: RouteInfo) =
        getParam(routeInfo) {
            distance[DISTANCE_VALUE]?.toDouble()
        }

    private fun getDuration(routeInfo: RouteInfo) =
        getParam(routeInfo) {
            duration[DURATION_VALUE]?.toDouble()
        }

    private fun getPolyline(routeInfo: RouteInfo) =
        routeInfo.legs
            .flatMap { leg ->
                leg.steps
            }
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


    private fun getParam(routeInfo: RouteInfo, supplier: LegInfo.() -> Double?) =
        routeInfo.legs
            .mapNotNull { leg ->
                supplier.invoke(leg)
            }
            .sumByDouble { it }


    inner class Summary(
        val distance: Double = 0.0,
        val time: Double = 0.0,
        val polyline: Array<LatLng> = arrayOf()
    )

}