package ua.com.cuteteam.cutetaxiproject.api.geocoding

import retrofit2.http.GET
import retrofit2.http.Query
import ua.com.cuteteam.cutetaxiproject.api.APIService

interface GeocodeService : APIService {
    @GET
    suspend fun getNameByCoordinates(@Query("latlng") latlng: String): Geocode

    @GET
    suspend fun getCoordinatesByName(@Query("address", encoded = true) address: String): Geocode
}