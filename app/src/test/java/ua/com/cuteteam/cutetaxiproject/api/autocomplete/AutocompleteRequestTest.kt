package ua.com.cuteteam.cutetaxiproject.api.autocomplete

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test

class AutocompleteRequestTest {

    private var autocompleteRequest: AutocompleteRequest? = null

    @Before
    fun init() {
        autocompleteRequest = AutocompleteRequest.Builder().build()
    }

    @After
    fun close() {
        autocompleteRequest = null
    }

    @Test
    fun getUrl() {
        MatcherAssert.assertThat(
            autocompleteRequest?.url,
            Matchers.equalTo("https://maps.googleapis.com/maps/api/place/autocomplete/")
        )
    }
}