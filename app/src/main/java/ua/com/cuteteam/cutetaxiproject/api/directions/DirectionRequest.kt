package ua.com.cuteteam.cutetaxiproject.api.directions

import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest

/**
 *
 * Do request for directions with this class
 *
 */
class DirectionRequest : APIRequest<DirectionService>() {

    /**
     * Base url for API request. Used by Retrofit
     *
     */
    override val url: String
        get() = BuildConfig.GOOGLE_DIRECTIONS_API_URL

    /**
     *
     * Suspend function. Does request to the server for direction between two locations
     * You need to build DirectionRequest with [DirectionRequest.Builder].
     * It builds [DirectionRequest] with a map of request parameters.
     * Anouther way is to create a DirectionRequest with a map using a
     * secondary constructor DirectionRequest(map: Map<String, String>)
     *
     */
    suspend fun requestDirection(map: Map<String, String>) =
        getService<DirectionService>().getDirection(map)

}