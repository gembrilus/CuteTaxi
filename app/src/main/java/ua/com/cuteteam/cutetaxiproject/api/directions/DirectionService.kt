package ua.com.cuteteam.cutetaxiproject.api.directions

import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.com.cuteteam.cutetaxiproject.api.APIService

/**
 * Retrofit service. Don't use it alone
 */
interface DirectionService : APIService {

    @GET(value = "json")
    suspend fun getDirection(@QueryMap(encoded = true) map: Map<String, String>): Route

}