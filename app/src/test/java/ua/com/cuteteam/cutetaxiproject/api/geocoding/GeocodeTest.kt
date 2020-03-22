package ua.com.cuteteam.cutetaxiproject.api.geocoding

import org.junit.Test
import kotlin.reflect.KVariance
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.primaryConstructor

class GeocodeTest {


    @Test
    fun classGeocodeTest() {


        assert(Geocode::class.isData)
        assert(Geocode::class.isFinal)
        assert(Geocode::class.constructors.size == 1)
        assert(Geocode::class.primaryConstructor != null)
        assert(Geocode::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Geocode::class.declaredMemberProperties.size == 2)

    }

    @Test
    fun getResults() {

        assert(Geocode::results.isFinal)
        assert(Geocode::results.returnType.arguments.size == 1)
        assert(Geocode::results.returnType.arguments[0].variance == KVariance.INVARIANT)
        assert(Geocode::results.returnType.arguments[0].type == Results::class.defaultType)
        assert(Geocode::results.visibility == KVisibility.PUBLIC)
        assert(Geocode::results.returnType.classifier == List::class)
        assert(!Geocode::results.returnType.isMarkedNullable)

    }

    @Test
    fun getStatus() {

        assert(Geocode::status.isFinal)
        assert(Geocode::status.returnType.classifier == String::class)
        assert(Geocode::status.visibility == KVisibility.PUBLIC)
        assert(!Geocode::status.returnType.isMarkedNullable)

    }
}