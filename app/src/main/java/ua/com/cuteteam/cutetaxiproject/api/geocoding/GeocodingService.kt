package ua.com.cuteteam.cutetaxiproject.api.geocoding

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ua.com.cuteteam.cutetaxiproject.api.APIService

/**
 * Retrofit service. Don't use it alone
 */
interface GeocodeService : APIService {

    @GET(value = "json")
    suspend fun getNameByCoordinates(
        @Query("latlng") latlng: String,
        @QueryMap map: Map<String, String>
    ): Geocode

    @GET(value = "json")
    suspend fun getCoordinatesByName(
        @Query("address", encoded = true) address: String,
        @QueryMap map: Map<String, String>
    ): Geocode

}