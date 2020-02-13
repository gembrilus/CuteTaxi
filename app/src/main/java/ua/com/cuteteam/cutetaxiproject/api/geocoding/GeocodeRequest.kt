package ua.com.cuteteam.cutetaxiproject.api.geocoding

import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*

class GeocodeRequest() : APIRequest<GeocodeService>() {

    internal constructor(map: Map<String, String>): this(){
        this.map = map
    }

    private lateinit var map: Map<String, String>

    override val url: String
        get() = BuildConfig.GOOGLE_GEO_API_URL

    suspend fun requestNameByCoordinates(lat: Double, lon: Double) =
        getService<GeocodeService>().getNameByCoordinates("$lat, $lon", map)

    suspend fun requestCoordinatesByName(address: String) =
        getService<GeocodeService>().getCoordinatesByName(address, map)

    class Builder {

        private var language: String = Locale.getDefault().language
        private var region: String = ""

        fun setLanguageResponse(lang: String) = apply {
            language = lang
        }

        fun setRegion(_region: String) = apply {
            region = _region
        }

        fun build(): GeocodeRequest {
            val map = mutableMapOf<String, String>()
            map[RequestParameters.LANG] = language
            if (region.isNotEmpty()){
                map[RequestParameters.REGION] = region
            }
            return GeocodeRequest(map)
        }

    }

}