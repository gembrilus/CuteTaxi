package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class DistanceTest {

    @Test
    fun classDistanceTest() {


        assert(Distance::class.isData)
        assert(Distance::class.isFinal)
        assert(Distance::class.constructors.size == 1)
        assert(Distance::class.primaryConstructor != null)
        assert(Distance::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Distance::class.declaredMemberProperties.size == 2)

    }

    @Test
    fun getValue() {

        assert(Distance::value.isFinal)
        assert(Distance::value.visibility == KVisibility.PUBLIC)
        assert(Distance::value.returnType.classifier == Double::class)
        assert(!Distance::value.returnType.isMarkedNullable)
    }

    @Test
    fun getText() {

        assert(Distance::text.isFinal)
        assert(Distance::text.visibility == KVisibility.PUBLIC)
        assert(Distance::text.returnType.classifier == String::class)
        assert(!Distance::text.returnType.isMarkedNullable)

    }
}