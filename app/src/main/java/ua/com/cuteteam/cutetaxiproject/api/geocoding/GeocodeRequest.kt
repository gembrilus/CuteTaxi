package ua.com.cuteteam.cutetaxiproject.api.geocoding

import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest

class GeocodeRequest : APIRequest<GeocodeService>() {

    override val url: String
        get() = BuildConfig.GOOGLE_GEO_API_URL

    suspend fun requestPlace(coords: Map<String, Double>) = withContext(IO){
        val latLng = "${coords["lat"]},${coords["lon"]}"
        getService<GeocodeService>().getNameByCoordinates(latLng)
    }

    suspend fun requestCoordinates(address: String) = withContext(IO){
        getService<GeocodeService>().getCoordinatesByName(address)
    }

}