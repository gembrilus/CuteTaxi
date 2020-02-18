package ua.com.cuteteam.cutetaxiproject.api.geocoding

import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class ResultsTest {


    @Test
    fun classResultsTest() {

        assert(Results::class.isData)
        assert(Results::class.isFinal)
        assert(Results::class.constructors.size == 1)
        assert(Results::class.primaryConstructor != null)
        assert(Results::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Results::class.declaredMemberProperties.size == 2)

    }


    @Test
    fun getFormattedAddress() {

        assert(Results::formattedAddress.isFinal)
        assert(Results::formattedAddress.returnType.classifier == String::class)
        assert(Results::formattedAddress.visibility == KVisibility.PUBLIC)
        assert(!Results::formattedAddress.returnType.isMarkedNullable)

    }

    @Test
    fun getGeometry() {

        assert(Results::geometry.isFinal)
        assert(Results::geometry.visibility == KVisibility.PUBLIC)
        assert(Results::geometry.returnType.classifier == Geometry::class)
        assert(!Results::geometry.returnType.isMarkedNullable)

    }
}