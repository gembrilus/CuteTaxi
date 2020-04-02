package ua.com.cuteteam.cutetaxiproject.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.com.cuteteam.cutetaxiproject.api.adapters.ManeuverAdapter
import ua.com.cuteteam.cutetaxiproject.api.interceptors.AuthInterceptor
import ua.com.cuteteam.cutetaxiproject.application.AppClass

/**
 * Base abstract class for Request classes: [DirectionRequest] and [GeocodeRequest]
 * Extend this class if you add new service
 */
abstract class APIRequest<T> where T: APIService {

    abstract val url: String
    abstract val adapter: Any?

    protected val IO = Dispatchers.IO

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(ManeuverAdapter())
        .apply {
            adapter?.let { add(it) }
        }
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(AuthInterceptor())
        .addInterceptor(ChuckerInterceptor(AppClass.appContext()))
        .build()

    protected fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    protected inline fun <reified T> getService(): T = buildRetrofit().create(T::class.java)

}