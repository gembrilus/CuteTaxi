package ua.com.cuteteam.cutetaxiproject.api.geocoding

import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*

class GeocodeRequest() : APIRequest<GeocodeService>() {

    internal constructor(map: Map<String, String>) : this() {
        _map = map
    }

    private var _map: Map<String, String>? = null
    private val map
        get() = _map
            ?: throw IllegalArgumentException(
                "You need to use Builder for building a request" +
                        "or use parametrized constructor GeocodeRequest(map: Map<String, String>)"
            )


    override val url: String
        get() = BuildConfig.GOOGLE_GEO_API_URL


    /**
     * Method receives human-readable name of map objects by coordinates.
     * Can to throw an exception. You must use try .. catch or CoroutineHandlerException
     * @param lat Latitude
     * @param lon Longitude
     * @return Geocode with all info you need or throw any exception when a request fails
     */
    suspend fun requestNameByCoordinates(lat: Double, lon: Double) =
        getService<GeocodeService>().getNameByCoordinates("$lat, $lon", map)


    /**
     * Method receives coordinates by name of map objects
     * Can to throw an exception. You must use try .. catch or CoroutineHandlerException
     * @param address It is a string address in free style
     * @return Geocode with all you needed info or throw any exception when a request fails
     */
    suspend fun requestCoordinatesByName(address: String) =
        getService<GeocodeService>().getCoordinatesByName(address, map)


    /**
     * Build a map of request parameters such as language and region
     *
     */
    class Builder {

        private var language: String = Locale.getDefault().language
        private var region: String = ""


        /**
         * It's a builder-method. It sets a language of API response.
         * @param lang in format ua, ru, en...
         */
        fun setLanguageResponse(lang: String) = apply {
            language = lang
        }


        /**
         * It's a builder-method. It sets a region of API response.
         * It needs because it can be many cities with the same name in different regions
         * @param _region in format ua, ru, uk...
         */
        fun setRegion(_region: String) = apply {
            region = _region
        }


        /**
         * Method that terminates building and return an instance of object for request
         * @return GeocodeRequest
         */
        fun build(): GeocodeRequest {
            val map = mutableMapOf<String, String>()
            map[RequestParameters.LANG] = language
            if (region.isNotEmpty()) {
                map[RequestParameters.REGION] = region
            }
            return GeocodeRequest(map)
        }

    }

}