package ua.com.cuteteam.cutetaxiproject.api.adapters

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import ua.com.cuteteam.cutetaxiproject.api.directions.Maneuver
import kotlin.reflect.KVisibility
import kotlin.reflect.full.*

class ManeuverAdapterTest {

    private var adapter: ManeuverAdapter? = null
    private val testedValue =
        Maneuver.FERRY

    @Before
    fun init() {
        adapter = ManeuverAdapter()
    }

    @After
    fun close() {
        adapter = null
    }

    @Test
    fun classManeuverAdapterTest() {

        assertThat(ManeuverAdapter::class.isFinal, Matchers.equalTo(true))
        assert(ManeuverAdapter::class.constructors.size == 1)
        assert(ManeuverAdapter::class.primaryConstructor != null)
        assert(ManeuverAdapter::class.primaryConstructor?.visibility == KVisibility.PUBLIC)


        assert(ManeuverAdapter::class.declaredFunctions.size == 2)
        assert(
            ManeuverAdapter::class.declaredFunctions.contains(
                ManeuverAdapter::toJson))
        assert(
            ManeuverAdapter::class.declaredFunctions.contains(
                ManeuverAdapter::fromJson))

        assert(ManeuverAdapter::toJson.visibility == KVisibility.PUBLIC)
        assert(ManeuverAdapter::toJson.returnType.classifier == String::class)
        assert(ManeuverAdapter::toJson.returnType.isMarkedNullable)
        assertThat(ManeuverAdapter::toJson.valueParameters.count(), Matchers.equalTo(1))
        assertThat(ManeuverAdapter::toJson.valueParameters[0].type.isMarkedNullable, Matchers.equalTo(true))
        assertThat(ManeuverAdapter::toJson.valueParameters[0].type.withNullability(false), Matchers.equalTo(
            Maneuver::class.defaultType))

        assert(ManeuverAdapter::fromJson.visibility == KVisibility.PUBLIC)
        assert(ManeuverAdapter::fromJson.returnType.classifier == Maneuver::class)
        assert(ManeuverAdapter::fromJson.returnType.isMarkedNullable)
        assertThat(ManeuverAdapter::fromJson.valueParameters.count(), Matchers.equalTo(1))
        assertThat(ManeuverAdapter::fromJson.valueParameters[0].type.isMarkedNullable, Matchers.equalTo(false))
        assertThat(ManeuverAdapter::fromJson.valueParameters[0].type, Matchers.equalTo(String::class.defaultType))

    }


    @Test
    fun toJson() {

        val actual = testedValue.maneuver
        val result = adapter?.toJson(testedValue)

        assertThat(actual, Matchers.equalTo(result))

    }

    @Test
    fun fromJson() {

        val actual = testedValue
        val result = adapter?.fromJson(testedValue.maneuver)

        assertThat(actual, Matchers.equalTo(result))

    }
}