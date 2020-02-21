package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVisibility

class PolylineTest {

    @Test
    fun getPoints() {

        assert(Polyline::points.isFinal)
        assert(Polyline::points.returnType.classifier == String::class)
        assert(Polyline::points.visibility == KVisibility.PUBLIC)
        assert(!Polyline::points.returnType.isMarkedNullable)

    }
}