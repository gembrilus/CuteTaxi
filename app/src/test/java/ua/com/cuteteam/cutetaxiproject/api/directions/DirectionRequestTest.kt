package ua.com.cuteteam.cutetaxiproject.api.directions

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class DirectionRequestTest {

    private var directionRequest: DirectionRequest? = null

    @Before
    fun init() {
        directionRequest = DirectionRequest()
    }

    @After
    fun close() {
        directionRequest = null
    }

    @Test
    fun getUrl() {
        assertThat(directionRequest?.url, Matchers.equalTo("https://maps.googleapis.com/maps/api/directions/"))
    }

}