package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class LocationTest {


    @Test
    fun classLocationTest() {


        assert(Location::class.isData)
        assert(Location::class.isFinal)
        assert(Location::class.constructors.size == 1)
        assert(Location::class.primaryConstructor != null)
        assert(Location::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Location::class.declaredMemberProperties.size == 2)

    }


    @Test
    fun getLatitude() {

        assert(Location::latitude.isFinal)
        assert(Location::latitude.returnType.classifier == Double::class)
        assert(Location::latitude.visibility == KVisibility.PUBLIC)
        assert(!Location::latitude.returnType.isMarkedNullable)

    }

    @Test
    fun getLongitude() {

        assert(Location::longitude.isFinal)
        assert(Location::longitude.returnType.classifier == Double::class)
        assert(Location::longitude.visibility == KVisibility.PUBLIC)
        assert(!Location::longitude.returnType.isMarkedNullable)

    }
}