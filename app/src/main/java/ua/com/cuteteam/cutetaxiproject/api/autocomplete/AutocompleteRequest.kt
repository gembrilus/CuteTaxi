package ua.com.cuteteam.cutetaxiproject.api.autocomplete

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*


/**
 *
 * Do request for autocomplete with this class
 * Class has a private constructor. Use Autocomplete.Builder() for building requests.
 */
class AutocompleteRequest private constructor(private val map: Map<String, String>) :
    APIRequest<AutoCompleteService>() {
    override val url: String
        get() = BuildConfig.GOOGLE_AUTOCOMPLETE_API_URL
    override val adapter: Any?
        get() = null


    /**
     * Method autocomplete your input by addresses.
     * Can to throw an exception. You must use try .. catch or CoroutineHandlerException
     * @param input is a text input, words, etc
     * @return List with names of the places throw any exception when a request fails
     */
    suspend fun autocomplete(input: String) = getService<AutoCompleteService>().complete(input, map)


    /**
     * Build a map of request parameters
     *
     */
    class Builder {

        private var language: String = Locale.getDefault().language
        private var location: String = ""
        private var radius: Int = -1


        /**
         * It's a builder-method. It sets a language of API response.
         * @param lang in format ua, ru, en...
         */
        fun setLanguageResponse(lang: String) = apply {
            language = lang
        }


        /**
         * It's a builder-method.
         * The point around which you wish to retrieve place information.
         * @param location is in a [LatLng] format
         */
        fun setLocation(location: LatLng) = apply {
            this.location = "${location.latitude},${location.longitude}"
        }


        /**
         * It's a builder-method.
         * The distance (in meters) within which to return place results.
         * Note that setting a radius biases results to the indicated area, but may not fully restrict results to the specified area.
         * @param radius is searching radius in meter
         */
        fun setRadius(radius: Int) = apply {
            this.radius = radius
        }


        /**
         * Method that terminates building and return an instance of object for request
         * @return AutocompleteRequest
         */
        fun build(): AutocompleteRequest {
            val map = mutableMapOf<String, String>()
            map[RequestParameters.LANG] = language
            if (location.isNotEmpty()) {
                map[RequestParameters.LOCATION] = location
            }
            if (radius > 0) {
                map[RequestParameters.RADIUS] = radius.toString()
            }
            return AutocompleteRequest(map)
        }

    }

}