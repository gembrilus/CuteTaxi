package ua.com.cuteteam.cutetaxiproject.common

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UtilsTest {

    private val order = Order(
        driverLocation = Coordinates(33.8207878, -117.9230963),
        addressStart = Address(
            location = Coordinates(34.0256221, -118.2059515)
        )
    )

    @Test
    fun calculatePrice() {
        // GIVEN
        val tax = 5.0
        val distance = 10.0
        val expectedStandard = 50.0
        val expectedComfort = 62.5
        val expectedEco = 75.0

        //WHEN
        val priceStandard = calculatePrice(tax, distance, ComfortLevel.STANDARD)
        val priceComfort = calculatePrice(tax, distance, ComfortLevel.COMFORT)
        val priceEco = calculatePrice(tax, distance, ComfortLevel.ECO)

        //THEN

        assertThat(expectedStandard, Matchers.equalTo(priceStandard))
        assertThat(expectedComfort, Matchers.equalTo(priceComfort))
        assertThat(expectedEco, Matchers.equalTo(priceEco))
    }

    @Test
    fun calculatePrice2() {
        // GIVEN
        val tax = 5.0
        val distance = 1.0
        val expectedStandard = 5.0
        val expectedComfort = 6.25
        val expectedEco = 7.5

        //WHEN
        val priceStandard = calculatePrice(tax, distance, ComfortLevel.STANDARD)
        val priceComfort = calculatePrice(tax, distance, ComfortLevel.COMFORT)
        val priceEco = calculatePrice(tax, distance, ComfortLevel.ECO)

        //THEN

        assertThat(expectedStandard, Matchers.equalTo(priceStandard))
        assertThat(expectedComfort, Matchers.equalTo(priceComfort))
        assertThat(expectedEco, Matchers.equalTo(priceEco))
    }

    @Test
    fun calcDistance() {
        //Given
        val expected = 34650.0

        //WHEN

        val actual = calcDistance(order)

        //THEN
        assertEquals(expected, actual!!, 1.0)

    }

    @Test
    fun prepareDistance() {
        //GIVEN
        val context = InstrumentationRegistry.getInstrumentation().context
        val expected = "34 km"
        //WHEN

        val actual = prepareDistance(context, order)

        //THEN

        assertThat(expected, Matchers.equalTo(actual))

    }

    @Test
    fun arrivalTime() {
        //GIVEN
        val expected = 34L

        //WHEN
        val actual = arrivalTime(order)

        //THEN
        assertThat(expected, Matchers.equalTo(actual!!))
    }
}