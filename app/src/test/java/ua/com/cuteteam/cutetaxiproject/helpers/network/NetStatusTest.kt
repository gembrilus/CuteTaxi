package ua.com.cuteteam.cutetaxiproject.helpers.network

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test

class NetStatusTest{

    private val statusNames = listOf(
        "AVAILABLE",
        "UNAVAILABLE",
        "LOST"
    )

    @Test
    fun getManeuver() {

        MatcherAssert.assertThat(NetStatus.values().size, Matchers.equalTo(3))
        MatcherAssert.assertThat(NetStatus.values().size, Matchers.equalTo(statusNames.size))

        val actual2 = NetStatus.values().map { it.name }
        val result2 = statusNames.toTypedArray()

        MatcherAssert.assertThat(actual2, Matchers.contains(*result2))

    }

}