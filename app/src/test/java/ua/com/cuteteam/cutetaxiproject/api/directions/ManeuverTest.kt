package ua.com.cuteteam.cutetaxiproject.api.directions

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.*

class ManeuverTest {


    private val maneuvers = listOf(
        "turn-slight-left",
        "turn-sharp-left",
        "uturn-left",
        "turn-left",
        "turn-slight-right",
        "turn-sharp-right",
        "uturn-right",
        "turn-right",
        "straight",
        "ramp-left",
        "ramp-right",
        "merge",
        "fork-left",
        "fork-right",
        "ferry",
        "ferry-train",
        "roundabout-left",
        "roundabout-right"
    )

    private val maneuversNames = listOf(
        "TURN_SLIGHT_LEFT",
        "TURN_SHARP_LEFT",
        "UTURN_LEFT",
        "TURN_LEFT",
        "TURN_SLIGHT_RIGHT",
        "TURN_SHARP_RIGHT",
        "UTURN_RIGHT",
        "TURN_RIGHT",
        "STRAIGHT",
        "RAMP_LEFT",
        "RAMP_RIGHT",
        "MERGE",
        "FORK_LEFT",
        "FORK_RIGHT",
        "FERRY",
        "FERRY_TRAIN",
        "ROUND_ABOUT_LEFT",
        "ROUND_ABOUT_RIGHT"
    )


    @Test
    fun classManeuverTest() {

        assert(!Maneuver::class.isData)
        assert(Maneuver::class.isFinal)
        assert(Maneuver::class.constructors.size == 1)
        assert(Maneuver::class.primaryConstructor != null)
        assertThat(Maneuver::class.primaryConstructor?.visibility, Matchers.equalTo(KVisibility.PRIVATE))
        assertThat(Maneuver::class.isSubclassOf(Enum::class), Matchers.equalTo(true))
        assertThat(Maneuver::class.declaredMemberProperties.size, Matchers.equalTo(1))

    }

    @Test
    fun getManeuver() {

        assertThat(Maneuver.values().size, Matchers.equalTo(18))
        assertThat(Maneuver.values().size, Matchers.equalTo(maneuvers.size))
        assertThat(Maneuver.values().size, Matchers.equalTo(maneuversNames.size))
        assertThat(Maneuver.values().map { it.maneuver }, Matchers.equalTo(maneuvers))

        val actual1 = Maneuver.values().map { it.maneuver }
        val result1 = maneuvers.toTypedArray()

        assertThat(actual1, Matchers.contains(*result1))

        val actual2 = Maneuver.values().map { it.name }
        val result2 = maneuversNames.toTypedArray()

        assertThat(actual2, Matchers.contains(*result2))

        assert(Maneuver.values().map { it.maneuver }.all { it::class == String::class })
        assert(Maneuver.values().all { it::class == Maneuver::class })

    }
}