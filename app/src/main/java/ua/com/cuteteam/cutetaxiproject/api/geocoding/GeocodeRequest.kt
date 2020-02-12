package ua.com.cuteteam.cutetaxiproject.api.geocoding

import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.RequestParameters
import java.util.*

class GeocodeRequest : APIRequest<GeocodeService>() {

    override val url: String
        get() = BuildConfig.GOOGLE_GEO_API_URL

    suspend fun fullRequestNameByCoordinates(lat: Double, lon: Double, map: Map<String, String>) =
        withContext(IO) {
            getService<GeocodeService>().getNameByCoordinates("$lat, $lon", map)
        }

    suspend fun fullRequestCoordinatesByName(address: String, map: Map<String, String>) =
        withContext(IO) {
            getService<GeocodeService>().getCoordinatesByName(address, map)
        }

    suspend fun requestNameByCoordinates(lat: Double, lon: Double) = withContext(IO) {
        val map = Builder().build()
        getService<GeocodeService>().getNameByCoordinates("$lat, $lon", map)
    }

    suspend fun requestCoordinatesByName(address: String) = withContext(IO) {
        val map = Builder().build()
        getService<GeocodeService>().getCoordinatesByName(address, map)
    }


    class Builder {

        private var language: String = Locale.getDefault().language
        private var region: String = Locale.getDefault().country.toLowerCase(Locale.ENGLISH)

        fun setLanguageResponse(lang: String) = apply {
            language = lang
        }

        fun setRegion(_region: String) = apply {
            region = _region
        }

        fun build(): Map<String, String> = mapOf(
            RequestParameters.LANG to language,
            RequestParameters.REGION to region
        )

    }

}