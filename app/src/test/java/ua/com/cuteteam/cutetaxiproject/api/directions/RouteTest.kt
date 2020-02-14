package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVariance
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.primaryConstructor

class RouteTest {

    @Test
    fun classRouteTest() {

        assert(Route::class.isData)
        assert(Route::class.isFinal)
        assert(Route::class.constructors.size == 1)
        assert(Route::class.primaryConstructor != null)
        assert(Route::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Route::class.declaredMemberProperties.size == 2)

    }

    @Test
    fun getStatus() {

        assert(Route::status.isFinal)
        assert(Route::status.returnType.classifier == String::class)
        assert(Route::status.visibility == KVisibility.PUBLIC)
        assert(!Route::status.returnType.isMarkedNullable)

    }


    @Test
    fun getRoutes() {

        assert(Route::routes.isFinal)
        assert(Route::routes.returnType.arguments.size == 1)
        assert(Route::routes.returnType.arguments[0].variance == KVariance.INVARIANT)
        assert(Route::routes.returnType.arguments[0].type == RouteInfo::class.defaultType)
        assert(Route::routes.returnType.classifier == List::class)
        assert(Route::routes.visibility == KVisibility.PUBLIC)
        assert(!Route::routes.returnType.isMarkedNullable)

    }
}