package ua.com.cuteteam.cutetaxiproject.api.autocomplete

import org.hamcrest.Matchers
import org.junit.Test

import org.junit.Assert.*

class AutocompleteTest {

    @Test
    fun getPlaces() {

        //GIVEN
        val autocomplete = Autocomplete(
            status = "OK",
            predictions = listOf(
                Place(description = "Cherkasy"),
                Place(description = "Kiev")
            )
        )

        val expected = listOf("Cherkasy", "Kiev")

        //WHEN

        val actual = autocomplete.places

        //THEN

        assertThat(expected, Matchers.equalTo(actual))

    }
}