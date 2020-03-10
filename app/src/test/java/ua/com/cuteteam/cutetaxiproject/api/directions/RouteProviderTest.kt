package ua.com.cuteteam.cutetaxiproject.api.directions

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.api.roads.RoadNode
import ua.com.cuteteam.cutetaxiproject.api.roads.Roads
import ua.com.cuteteam.cutetaxiproject.api.roads.RoadsRequest


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RouteProviderTest {

    private var routeBuilder: RouteProvider.Builder? = null
    private var route: Route? = null
    private var directionRequest: DirectionRequest? = null
    private var roadsRequest: RoadsRequest? = null

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
                                Step(
                                    startLocation = LatLng(12.0, 12.0),
                                    endLocation = LatLng(15.0, 14.0),
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
                    ),
                    polyline = Polyline(
                        points = ""
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
                                Step(
                                    startLocation = LatLng(12.0, 12.0),
                                    endLocation = LatLng(15.0, 14.0),
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
                    ),
                    polyline = Polyline(
                        points = ""
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
                                Step(
                                    startLocation = LatLng(12.0, 12.0),
                                    endLocation = LatLng(15.0, 14.0),
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
                    ),
                    polyline = Polyline(
                        points = ""
                    )
                )
            )
        )

        directionRequest = mock {
            on { runBlocking { requestDirection(any()) } } doReturn route!!
        }

        roadsRequest = mock {
            on {
                runBlocking {
                    getRoads(any())
                }
            } doReturn Roads(
                snappedPoints = listOf(
                    RoadNode(any(), any())
                )
            )
        }

        routeBuilder = route?.let {
            RouteProvider.Builder()
        }
    }

    @After
    fun close() {
        route = null
        directionRequest = null
        routeBuilder = null
    }

  /*  @Test
    fun findTheFastest() {

        val actual = listOf(
            routeBuilder?.build()?.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result = routeBuilder!!.findTheFastest().build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun findTheShortest() {

        val actual = listOf(
            routeBuilder?.build()?.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result = routeBuilder?.findTheShortest()?.build()

        assertThat(actual, Matchers.equalTo(result))

    }

    @Test
    fun findTheFastestAndTheShortestWays() {
        val actual = listOf(
            routeBuilder?.build()?.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            routeBuilder?.build()?.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result = routeBuilder
            ?.findTheShortest()
            ?.findTheFastest()
            ?.build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun build() {

        val actual = listOf(
            routeBuilder?.build()?.RouteSummary(
                distance = 999.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            routeBuilder?.build()?.RouteSummary(
                distance = 1000.0,
                time = 58.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            ),
            routeBuilder?.build()?.RouteSummary(
                distance = 1001.0,
                time = 60.0,
                polyline = arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
                maneuvers = arrayListOf(Maneuver.STRAIGHT),
                instructions = arrayListOf("bla bla car")
            )
        )
        val result = routeBuilder!!.build()

        assertThat(actual, Matchers.equalTo(result))
    }

    @Test
    fun testThatBuildAllPolylines() {

        val actual: List<Array<LatLng>> = arrayListOf(
            arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
            arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0)),
            arrayOf(LatLng(12.0, 12.0), LatLng(15.0, 14.0))
        )
        val result = mutableListOf(
            *routeBuilder?.build()?.getManeuverPoints(route!!.routes[0])?.toTypedArray()!!,
            *routeBuilder?.build()?.getManeuverPoints(route!!.routes[1])?.toTypedArray()!!,
            *routeBuilder?.build()?.getManeuverPoints(route!!.routes[2])?.toTypedArray()!!
        )

        val transformActual = actual
            .flatMap { latlng -> latlng.toList() }
            .map { "${it.latitude}, ${it.longitude}" }

        val transformResult =
            result.map { "${it.latitude}, ${it.longitude}" }

        assertThat(transformActual, Matchers.equalTo(transformResult))
    }
*/
}
