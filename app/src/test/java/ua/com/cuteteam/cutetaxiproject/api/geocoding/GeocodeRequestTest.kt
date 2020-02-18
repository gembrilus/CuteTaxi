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

    @Before
    fun init() {
        geocodeRequest = GeocodeRequest()
    }

    @After
    fun close() {
        geocodeRequest = null
    }


    @Test
    fun classGeocodeRequestTest() {

        MatcherAssert.assertThat(GeocodeRequest::class.isFinal, Matchers.equalTo(true))
        assert(GeocodeRequest::class.constructors.size == 2)
        assert(GeocodeRequest::class.constructors.any { it.valueParameters.size == 1 })
        assert(GeocodeRequest::class.primaryConstructor != null)
        assert(GeocodeRequest::class.primaryConstructor?.visibility == KVisibility.PUBLIC)

        assert(GeocodeRequest::class.declaredFunctions.size == 2)
        assert(GeocodeRequest::class.declaredFunctions.contains(GeocodeRequest::requestNameByCoordinates))
        assert(GeocodeRequest::class.declaredFunctions.contains(GeocodeRequest::requestCoordinatesByName))

    }


    @Test
    fun getUrl() {
        MatcherAssert.assertThat(
            geocodeRequest?.url,
            Matchers.equalTo("https://maps.googleapis.com/maps/api/geocode/")
        )
    }

    @Test
    fun requestNameByCoordinates() {

        assert(GeocodeRequest::requestNameByCoordinates.isSuspend)
        assert(GeocodeRequest::requestNameByCoordinates.visibility == KVisibility.PUBLIC)
        assert(GeocodeRequest::requestNameByCoordinates.returnType.classifier == Geocode::class)
        assert(!GeocodeRequest::requestNameByCoordinates.returnType.isMarkedNullable)
        assert(GeocodeRequest::requestNameByCoordinates.valueParameters.count() == 2)
        assert(GeocodeRequest::requestNameByCoordinates.valueParameters.all { it.type == Double::class.defaultType })

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