package ua.com.cuteteam.cutetaxiproject.api.directions

import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.BuildConfig
import ua.com.cuteteam.cutetaxiproject.api.APIRequest

class DirectionRequest: APIRequest<DirectionService>() {

    override val url: String
        get() = BuildConfig.GOOGLE_DIRECTIONS_API_URL

    suspend fun requestDirection(map: Map<String, String>) = withContext(IO){
        getService<DirectionService>().getDirection(map)
    }
}