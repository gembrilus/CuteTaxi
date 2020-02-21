package ua.com.cuteteam.cutetaxiproject.api.directions

import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KVariance
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.primaryConstructor

class RouteInfoTest {


    @Test
    fun classRouteInfoTest() {

        assert(RouteInfo::class.isData)
        assert(RouteInfo::class.isFinal)
        assert(RouteInfo::class.constructors.size == 1)
        assert(RouteInfo::class.primaryConstructor != null)
        assert(RouteInfo::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(RouteInfo::class.declaredMemberProperties.size == 3)

    }


    @Test
    fun getSummary() {

        assert(RouteInfo::summary.isFinal)
        assert(RouteInfo::summary.returnType.classifier == String::class)
        assert(RouteInfo::summary.visibility == KVisibility.PUBLIC)
        assert(!RouteInfo::summary.returnType.isMarkedNullable)

    }

    @Test
    fun getLegs() {

        assert(RouteInfo::legs.isFinal)
        assert(RouteInfo::legs.returnType.arguments.size == 1)
        assert(RouteInfo::legs.returnType.arguments[0].variance == KVariance.INVARIANT)
        assertThat(RouteInfo::legs.returnType.arguments[0].type, Matchers.equalTo(LegInfo::class.defaultType))
        assert(RouteInfo::legs.returnType.classifier == List::class)
        assert(RouteInfo::legs.visibility == KVisibility.PUBLIC)
        assert(!RouteInfo::legs.returnType.isMarkedNullable)

    }

    @Test
    fun getPolyline(){
        assert(RouteInfo::polyline.isFinal)
        assert(RouteInfo::polyline.returnType.classifier == Polyline::class)
        assert(RouteInfo::polyline.visibility == KVisibility.PUBLIC)
        assert(!RouteInfo::polyline.returnType.isMarkedNullable)
    }
}