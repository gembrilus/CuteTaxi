package ua.com.cuteteam.cutetaxiproject.api.geocoding

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class GeocodeRequestTest {

    private var geocodeRequest: GeocodeRequest? = null

    private val geocodeFuns = listOf(
        "requestNameByCoordinates",
        "requestCoordinatesByName"
    )

    @Before
    fun init() {
        geocodeRequest = GeocodeRequest.Builder().build()
    }

    @After
    fun close() {
        geocodeRequest = null
    }


    @Test
    fun classGeocodeRequestTest() {

        MatcherAssert.assertThat(GeocodeRequest::class.isFinal, Matchers.equalTo(true))
        assert(GeocodeRequest::class.constructors.size == 1)
        assert(GeocodeRequest::class.constructors.any { it.valueParameters.size == 1 })
        assert(GeocodeRequest::class.primaryConstructor != null)
        assert(GeocodeRequest::class.primaryConstructor?.visibility == KVisibility.PRIVATE)
        assert(GeocodeRequest::class.declaredFunctions.size == 4)
        assert(GeocodeRequest::class.declaredFunctions.all { geocodeFuns.contains(it.name) })

    }


    @Test
    fun getUrl() {
        MatcherAssert.assertThat(
            geocodeRequest?.url,
            Matchers.equalTo("https://maps.googleapis.com/maps/api/geocode/")
        )
    }


    @Test
    fun requestCoordinatesByName() {

        assert(GeocodeRequest::requestCoordinatesByName.isSuspend)
        assert(GeocodeRequest::requestCoordinatesByName.visibility == KVisibility.PUBLIC)
        assert(GeocodeRequest::requestCoordinatesByName.returnType.classifier == Geocode::class)
        assert(!GeocodeRequest::requestCoordinatesByName.returnType.isMarkedNullable)
        assert(GeocodeRequest::requestCoordinatesByName.valueParameters.count() == 1)
        assert(GeocodeRequest::requestCoordinatesByName.valueParameters.all { it.type == String::class.defaultType })

    }
}