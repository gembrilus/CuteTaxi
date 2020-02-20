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

    @Test
    fun requestDirection() {

        assert(DirectionRequest::requestDirection.isSuspend)
        assert(DirectionRequest::requestDirection.visibility == KVisibility.PUBLIC)
        assert(DirectionRequest::requestDirection.returnType.classifier == Route::class)
        assert(!DirectionRequest::requestDirection.returnType.isMarkedNullable)
        assert(DirectionRequest::requestDirection.valueParameters.count() == 1)

    }

    @Test
    fun classDirectionRequestTest() {

        assertThat(DirectionRequest::class.isFinal, Matchers.equalTo(true))
        assertThat(DirectionRequest::class.constructors.size, Matchers.equalTo(1))
        assertThat(DirectionRequest::class.constructors.any { it.valueParameters.size == 0 }, Matchers.`is`(true))
        assertThat(DirectionRequest::class.primaryConstructor != null, Matchers.`is`(true))
        assertThat(DirectionRequest::class.primaryConstructor?.visibility, Matchers.equalTo(KVisibility.PUBLIC))

        assert(DirectionRequest::class.declaredFunctions.size == 1)
        assert(DirectionRequest::class.declaredFunctions.contains(DirectionRequest::requestDirection))

    }

}