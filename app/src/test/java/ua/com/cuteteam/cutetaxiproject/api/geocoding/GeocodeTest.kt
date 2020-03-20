package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.google.android.gms.maps.model.LatLng
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates

class GeocodeTest {

    private val location get() = Coordinates(1.0, 1.0)
    private val location2 get() = LatLng(1.0, 1.0)
    private val name get() = "Address 10-1"

    private val address
        get() = Address(
            location = location,
            address = name
        )

    private val geocode
        get() = Geocode(
            results = listOf(
                Results(
                    formattedAddress = name,
                    geometry = Geometry(
                        location = location2
                    )
                )
            ),
            status = "OK"
        )


    @Test
    fun toLatLng() {
        assertThat(location2, Matchers.`is`(geocode.toLatLng()))
    }

    @Test
    fun toName() {
        assertThat(name, Matchers.`is`(geocode.toName()))
    }

    @Test
    fun toAddress() {
        assertThat(address, Matchers.`is`(geocode.toAddress()))
    }
}