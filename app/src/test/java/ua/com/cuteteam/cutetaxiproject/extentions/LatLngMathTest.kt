package ua.com.cuteteam.cutetaxiproject.extentions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LatLngMathTest {

    private val latLng1Start = LatLng(33.8207878, -117.9230963)
    private val latLng2End = LatLng(34.0256221, -118.2059515)

    private val latLng3Start = LatLng(33.8179846, -117.9223305)
    private val latLng4End = LatLng(33.8207878, -117.9230963)

    @Test
    fun calculate_Distance_between_coordinates_in_meters() {

        //Given
        val actual1 = latLng1Start distanceTo latLng2End
        val expected1 = 34650.0

        val actual2 = latLng3Start distanceTo latLng4End
        val expected2 = 320.0

        //Test
        assertEquals(expected1, actual1, 1.0)
        assertEquals(expected2, actual2, 1.0)
    }

    @Test
    fun test_convertation_Location_To_LatLng(){

        val location = mock<Location> {
            on { latitude } doReturn latLng1Start.latitude
            on { longitude } doReturn latLng1Start.longitude
        }
        val result = location.toLatLng

        assertEquals(location.latitude, result.latitude, 0.0001)
        assertEquals(location.longitude, result.longitude, 0.0001)

    }

}