package ua.com.cuteteam.cutetaxiproject.common.network

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

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