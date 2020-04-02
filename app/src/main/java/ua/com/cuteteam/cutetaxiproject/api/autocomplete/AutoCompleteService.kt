package ua.com.cuteteam.cutetaxiproject.api.autocomplete

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ua.com.cuteteam.cutetaxiproject.api.APIService

/**
 * Retrofit service. Don't use it alone
 */
interface AutoCompleteService : APIService {

    @GET(value = "json")
    suspend fun complete(
        @Query(value = "input", encoded = true) input: String,
        @QueryMap(encoded = true) map: Map<String, String>
    ): Autocomplete

}