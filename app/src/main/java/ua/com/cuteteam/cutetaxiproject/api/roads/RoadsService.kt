package ua.com.cuteteam.cutetaxiproject.api.roads

import retrofit2.http.GET
import retrofit2.http.Query
import ua.com.cuteteam.cutetaxiproject.api.APIService

/**
 * Retrofit service. Don't use it alone
 */
interface RoadsService : APIService {

    @GET(value = "snapToRoads")
    suspend fun getRoute(
        @Query("path") path: String,
        @Query("interpolate") interpolate: Boolean = true
    ): Roads

}