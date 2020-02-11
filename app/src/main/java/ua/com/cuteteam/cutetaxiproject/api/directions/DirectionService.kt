package ua.com.cuteteam.cutetaxiproject.api.directions

import retrofit2.http.GET
import retrofit2.http.QueryMap
import ua.com.cuteteam.cutetaxiproject.api.APIService

interface DirectionService : APIService {

    @GET
    suspend fun getDirection(@QueryMap map: Map<String, String>): Route

}