package ua.com.cuteteam.cutetaxiproject.api.geocoding

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import ua.com.cuteteam.cutetaxiproject.api.adapters.LatLngAdapter
import java.util.*


/**
 *
 * Do request for geocode with this class
 * Class has a private constructor. Use GeocodeRequest.Builder() for building requests.
 */
class GeocodeRequest private constructor(private val map: Map<String, String>) : APIRequest<GeocodeService>() {

    /**
     * Base url for API request. Used by Retrofit
     *
     */
    override val url: String
        get() = BuildConfig.GOOGLE_GEO_API_URL

    /**
     * Moshi adapter for convert Location to LatLng
     *
     */
    override val adapter: Any?
        get() = LatLngAdapter()

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
     * Overloaded method receives human-readable name of map object by coordinates given as LatLng.
     * Can to throw an exception. You must use try .. catch or CoroutineHandlerException
     * @param latLng Coordinates as [LatLng] class
     * @return Geocode with all info you need or throw any exception when a request fails
     */
    suspend fun requestNameByCoordinates(latLng: LatLng) =
        getService<GeocodeService>().getNameByCoordinates("${latLng.latitude}, ${latLng.longitude}", map)


    /**
     * Overloaded method receives human-readable name of map object by coordinates given as String.
     * An argument format is "12.121212,32.111234" for example
     * Can to throw an exception. You must use try .. catch or CoroutineHandlerException
     * @param lat Latitude
     * @param lon Longitude
     * @return Geocode with all info you need or throw any exception when a request fails
     */
    suspend fun requestNameByCoordinates(latLng: String) =
        getService<GeocodeService>().getNameByCoordinates(latLng, map)


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