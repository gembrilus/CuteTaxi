package ua.com.cuteteam.cutetaxiproject.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.com.cuteteam.cutetaxiproject.api.interceptors.AuthInterceptor

abstract class APIRequest<T> where T: APIService {

    object Units {
        const val METRIC = "metric"
        const val IMPERIAL = "imperial"
    }

    object Avoid {
        const val TOLLS = "tolls"
        const val HIGHWAYS = "highways"
        const val FERRIES = "ferries"
    }

    object RequestParameters {
        const val ORIGIN_PLACE = "origin"
        const val DESTINATION_PLACE = "destination"
        const val WAY_POINTS = "waypoints"
        const val IS_ALTERNATIVE_WAYS = "alternatives"
        const val AVOID = "avoid"
        const val LANG = "language"
        const val UNITS = "units"
        const val REGION = "region"
    }

    abstract val url: String

    protected val IO = Dispatchers.IO

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(AuthInterceptor())
        .build()

    protected fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    protected inline fun <reified T> getService(): T = buildRetrofit().create(T::class.java)

}