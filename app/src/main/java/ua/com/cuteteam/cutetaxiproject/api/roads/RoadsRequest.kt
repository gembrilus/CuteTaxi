package ua.com.cuteteam.cutetaxiproject.api.roads

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest

class RoadsRequest : APIRequest<RoadsService>(){
    override val url: String
        get() = BuildConfig.GOOGLE_ROADS_API_URL

    suspend fun getRoads(path: List<LatLng>) =
        getService<RoadsService>().getRoute(
            path.joinToString("|") {"${it.latitude},${it.longitude}"}
        )

}
