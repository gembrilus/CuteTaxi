package ua.com.cuteteam.cutetaxiproject.api.roads

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest
import ua.com.cuteteam.cutetaxiproject.api.directions.DirectionRequest


/**
 * Do request for snapping to roads
 */
class RoadsRequest : APIRequest<RoadsService>(){
    override val url: String
        get() = BuildConfig.GOOGLE_ROADS_API_URL


    /**
     * Moshi adapter no needs
     */
    override val adapter: Any?
        get() = null

    /**
     * Suspend function. Requests from the server coordinates with snapping to roads
     * @param path List of coordinates received with [DirectionRequest]
     * @return an instance of [Roads]
     */
    suspend fun getRoads(path: List<LatLng>) =
        getService<RoadsService>().getRoute(
            path.joinToString("|") {"${it.latitude},${it.longitude}"}
        )

}
