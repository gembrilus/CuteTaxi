package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVariance
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.primaryConstructor

class LegInfoTest {

    @Test
    fun classLegInfoTest() {


        assert(LegInfo::class.isData)
        assert(LegInfo::class.isFinal)
        assert(LegInfo::class.constructors.size == 1)
        assert(LegInfo::class.primaryConstructor != null)
        assert(LegInfo::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(LegInfo::class.declaredMemberProperties.size == 3)

    }

    @Test
    fun getSteps() {

        assert(LegInfo::steps.isFinal)
        assert(LegInfo::steps.returnType.arguments.size == 1)
        assert(LegInfo::steps.returnType.arguments[0].variance == KVariance.INVARIANT)
        assert(LegInfo::steps.returnType.arguments[0].type == StepInfo::class.defaultType)
        assert(LegInfo::steps.visibility == KVisibility.PUBLIC)
        assert(LegInfo::steps.returnType.classifier == List::class)
        assert(!LegInfo::steps.returnType.isMarkedNullable)

    }

    @Test
    fun getDuration() {

        assert(LegInfo::duration.isFinal)
        assert(LegInfo::duration.visibility == KVisibility.PUBLIC)
        assert(LegInfo::duration.returnType.classifier == Duration::class)
        assert(!LegInfo::duration.returnType.isMarkedNullable)

    }

    @Test
    fun getDistance() {

        assert(LegInfo::distance.isFinal)
        assert(LegInfo::distance.visibility == KVisibility.PUBLIC)
        assert(LegInfo::distance.returnType.classifier == Distance::class)
        assert(!LegInfo::distance.returnType.isMarkedNullable)

    }
}