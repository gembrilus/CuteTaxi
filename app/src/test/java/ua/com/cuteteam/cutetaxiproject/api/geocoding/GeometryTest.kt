package ua.com.cuteteam.cutetaxiproject.api.geocoding

import org.junit.Test
import ua.com.cuteteam.cutetaxiproject.api.directions.Location
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class GeometryTest {

    @Test
    fun classGeometryTest() {


        assert(Geometry::class.isData)
        assert(Geometry::class.isFinal)
        assert(Geometry::class.constructors.size == 1)
        assert(Geometry::class.primaryConstructor != null)
        assert(Geometry::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Geometry::class.declaredMemberProperties.size == 1)

    }

    @Test
    fun getLocation() {

        assert(Geometry::location.isFinal)
        assert(Geometry::location.visibility == KVisibility.PUBLIC)
        assert(Geometry::location.returnType.classifier == Location::class)
        assert(!Geometry::location.returnType.isMarkedNullable)

    }
}