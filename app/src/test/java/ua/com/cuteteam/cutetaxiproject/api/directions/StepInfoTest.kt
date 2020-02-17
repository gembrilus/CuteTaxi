package ua.com.cuteteam.cutetaxiproject.api.directions

import org.hamcrest.Condition
import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberExtensionProperties
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class StepInfoTest {


    @Test
    fun classStepInfoTest() {


        assert(StepInfo::class.isData)
        assert(StepInfo::class.isFinal)
        assert(StepInfo::class.constructors.size == 1)
        assert(StepInfo::class.primaryConstructor != null)
        assert(StepInfo::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(StepInfo::class.declaredMemberProperties.size == 6)

    }

    @Test
    fun getStartLocation() {

        assert(StepInfo::startLocation.isFinal)
        assert(StepInfo::startLocation.returnType.classifier == Location::class)
        assert(StepInfo::startLocation.visibility == KVisibility.PUBLIC)
        assert(!StepInfo::startLocation.returnType.isMarkedNullable)

    }

    @Test
    fun getEndLocation() {


        assert(StepInfo::endLocation.isFinal)
        assert(StepInfo::endLocation.returnType.classifier == Location::class)
        assert(StepInfo::endLocation.visibility == KVisibility.PUBLIC)
        assert(!StepInfo::endLocation.returnType.isMarkedNullable)

    }

    @Test
    fun getDuration() {


        assert(StepInfo::duration.isFinal)
        assert(StepInfo::duration.returnType.classifier == Duration::class)
        assert(StepInfo::duration.visibility == KVisibility.PUBLIC)
        assert(!StepInfo::duration.returnType.isMarkedNullable)

    }

    @Test
    fun getDistance() {

        assert(StepInfo::distance.isFinal)
        assert(StepInfo::distance.returnType.classifier == Distance::class)
        assert(StepInfo::distance.visibility == KVisibility.PUBLIC)
        assert(!StepInfo::distance.returnType.isMarkedNullable)

    }

    @Test
    fun getInstructions() {

        assert(StepInfo::instructions.isFinal)
        assert(StepInfo::instructions.returnType.classifier == String::class)
        assert(StepInfo::instructions.visibility == KVisibility.PUBLIC)
        assert(!StepInfo::instructions.returnType.isMarkedNullable)

    }

    @Test
    fun getManeuver() {

        assert(StepInfo::maneuver.isFinal)
        assert(StepInfo::maneuver.returnType.classifier == Maneuver::class)
        assert(StepInfo::maneuver.visibility == KVisibility.PUBLIC)
        assert(StepInfo::maneuver.returnType.isMarkedNullable)

    }
}