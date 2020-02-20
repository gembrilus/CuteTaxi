
package ua.com.cuteteam.cutetaxiproject.api.directions
/*
import com.google.android.gms.maps.model.LatLng
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider

class RouteProviderTest {


    private var routeProvider: RouteProvider? = null
    private var route: Route? = null


    @Before
    fun init() {
        route = Route(
            status = "OK",
            routes = listOf(
                RouteInfo(
                    summary = "I-14",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 60.0,
                                text = "1 min"
                            ),
                            distance = Distance(
                                value = 999.0,
                                text = "1 km"
                            ),
                            steps = listOf(
                                StepInfo(
                                    startLocation = Location(
                                        latitude = 12.0,
                                        longitude = 12.0
                                    ),
                                    endLocation = Location(
                                        latitude = 15.0,
                                        longitude = 14.0
                                    ),
                                    duration = Duration(
                                        value = 60.0,
                                        text = "1 min"
                                    ),
                                    distance = Distance(
                                        value = 1000.0,
                                        text = "1 km"
                                    ),
                                    instructions = "bla bla car",
                                    maneuver = Maneuver.STRAIGHT
                                )
                            )
                        )
                    )
                ),

                RouteInfo(
                    summary = "I-13",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 58.0,
                                text = "1 min"
                            ),
                            distance = Distance(
                                value = 1000.0,
                                text = "1 km"
                            ),
                            steps = listOf(
                                StepInfo(
                                    startLocation = Location(
                                        latitude = 12.0,
                                        longitude = 12.0
                                    ),
                                    endLocation = Location(
                                        latitude = 15.0,
                                        longitude = 14.0
                                    ),
                                    duration = Duration(
                                        value = 58.0,
                                        text = "1 min"
                                    ),
                                    distance = Distance(
                                        value = 1000.0,
                                        text = "1 km"
                                    ),
                                    instructions = "bla bla car",
                                    maneuver = Maneuver.STRAIGHT
                                )
                            )
                        )
                    )
                ),

                RouteInfo(
                    summary = "I-15",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 60.0,
                                text = "1 min"
                            ),
                            distance = Distance(
                                value = 1001.0,
                                text = "1 km"
                            ),
                            steps = listOf(
                                StepInfo(
                                    startLocation = Location(
                                        latitude = 12.0,
                                        longitude = 12.0
                                    ),
                                    endLocation = Location(
                                        latitude = 15.0,
                                        longitude = 14.0
                                    ),
                                    duration = Duration(
                                        value = 60.0,
                                        text = "1 min"
                                    ),
                                    distance = Distance(
                                        value = 1001.0,
                                        text = "1 km"
                                    ),
                                    instructions = "bla bla car",
                                    maneuver = Maneuver.STRAIGHT
                                )
                            )
                        )
                    )
                )
            )
        )
        routeProvider = route?.let {
            RouteProvider(
                it
            )
        }
    }

    @After
    fun close() {
        route = null
        routeProvider = null
    }

    @Test
    fun findTheFastest() {

        val actual = listOf(
            routeProvider!!.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result =  routeProvider!!.findTheFastest().build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun findTheShortest() {

        val actual = listOf(
            RouteProvider.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result =  routeProvider!!.findTheShortest().build()

        assertThat(actual, Matchers.equalTo(result))

    }

    @Test
    fun findTheFastestAndTheShortestWays() {
        val actual = listOf(
            RouteProvider.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            RouteProvider.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result =  routeProvider!!
            .findTheShortest()
            .findTheFastest()
            .build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun build() {

        val actual = listOf(
            RouteProvider.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            RouteProvider.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            RouteProvider.RouteSummary(
                distance = 1001.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result =  routeProvider!!.build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun testThatBuildAllPolylines() {

        val actual: List<Array<LatLng>> = arrayListOf(
                arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0))
        )
        val result =  routeProvider!!.buildPolylines()

        val transformActual = actual.flatMap { latlng -> latlng.toList() }.map { "${it.latitude}, ${it.longitude}" }
        val transformResult = result.flatMap { latlng -> latlng.toList() }.map { "${it.latitude}, ${it.longitude}" }

        assertThat(transformActual, Matchers.equalTo(transformResult))
    }

}*/
