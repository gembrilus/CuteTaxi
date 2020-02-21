package ua.com.cuteteam.cutetaxiproject.api.directions

import org.junit.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class StepTest {


    @Test
    fun classStepInfoTest() {


        assert(Step::class.isData)
        assert(Step::class.isFinal)
        assert(Step::class.constructors.size == 1)
        assert(Step::class.primaryConstructor != null)
        assert(Step::class.primaryConstructor?.visibility == KVisibility.PUBLIC)
        assert(Step::class.declaredMemberProperties.size == 6)

    }

    @Test
    fun getStartLocation() {

        assert(Step::startLocation.isFinal)
        assert(Step::startLocation.returnType.classifier == Location::class)
        assert(Step::startLocation.visibility == KVisibility.PUBLIC)
        assert(!Step::startLocation.returnType.isMarkedNullable)

    }

    @Test
    fun getEndLocation() {


        assert(Step::endLocation.isFinal)
        assert(Step::endLocation.returnType.classifier == Location::class)
        assert(Step::endLocation.visibility == KVisibility.PUBLIC)
        assert(!Step::endLocation.returnType.isMarkedNullable)

    }

    @Test
    fun getDuration() {


        assert(Step::duration.isFinal)
        assert(Step::duration.returnType.classifier == Duration::class)
        assert(Step::duration.visibility == KVisibility.PUBLIC)
        assert(!Step::duration.returnType.isMarkedNullable)

    }

    @Test
    fun getDistance() {

        assert(Step::distance.isFinal)
        assert(Step::distance.returnType.classifier == Distance::class)
        assert(Step::distance.visibility == KVisibility.PUBLIC)
        assert(!Step::distance.returnType.isMarkedNullable)

    }

    @Test
    fun getInstructions() {

        assert(Step::instructions.isFinal)
        assert(Step::instructions.returnType.classifier == String::class)
        assert(Step::instructions.visibility == KVisibility.PUBLIC)
        assert(!Step::instructions.returnType.isMarkedNullable)

    }

    @Test
    fun getManeuver() {

        assert(Step::maneuver.isFinal)
        assert(Step::maneuver.returnType.classifier == Maneuver::class)
        assert(Step::maneuver.visibility == KVisibility.PUBLIC)
        assert(Step::maneuver.returnType.isMarkedNullable)

    }
}