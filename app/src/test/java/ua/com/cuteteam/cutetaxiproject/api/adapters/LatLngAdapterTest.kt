package ua.com.cuteteam.cutetaxiproject.api.adapters

import com.google.android.gms.maps.model.LatLng
import org.hamcrest.Matchers
import org.junit.Test

import org.junit.Assert.*
import ua.com.cuteteam.cutetaxiproject.api.directions.Location

class LatLngAdapterTest {

    private val adapter = LatLngAdapter()

    @Test
    fun toJson() {

        //Given
        val latLng = LatLng(1.0, 1.0)

        //Run
        val result = adapter.toJson(latLng)

        //Assert
        assertThat(latLng.latitude, Matchers.equalTo(result.latitude))
        assertThat(latLng.longitude, Matchers.equalTo(result.longitude))

    }

    @Test
    fun fromJson() {

        //Given
        val location = Location(2.0, 2.0)

        //Run
        val result = adapter.fromJson(location)

        //Assert
        assertThat(location.latitude, Matchers.equalTo(result.latitude))
        assertThat(location.longitude, Matchers.equalTo(result.longitude))

    }
}