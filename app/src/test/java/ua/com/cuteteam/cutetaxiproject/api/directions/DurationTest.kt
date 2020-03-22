package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor


class DurationTest {

    @Test
    fun classDurationTest() {


        assert(Duration::class.isData)
        assert(Duration::class.isFinal)
        assert(Duration::class.constructors.size == 1)
        assert(Duration::class.primaryConstructor != null)
        assert(Duration::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Duration::class.declaredMemberProperties.size == 2)

    }

    @Test
    fun getValue() {

        assert(Duration::value.isFinal)
        assert(Duration::value.visibility == KVisibility.PUBLIC)
        assert(Duration::value.returnType.classifier == Double::class)
        assert(!Duration::value.returnType.isMarkedNullable)
    }

    @Test
    fun getText() {

        assert(Duration::text.isFinal)
        assert(Duration::text.visibility == KVisibility.PUBLIC)
        assert(Duration::text.returnType.classifier == String::class)
        assert(!Duration::text.returnType.isMarkedNullable)

    }
}