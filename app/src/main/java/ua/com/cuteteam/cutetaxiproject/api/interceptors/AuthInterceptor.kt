package ua.com.cuteteam.cutetaxiproject.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ua.com.cuteteam.cutetaxiproject.BuildConfig

private const val REQUEST_PARAM_GOOGLE_KEY = "key"

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addQueryParameter(REQUEST_PARAM_GOOGLE_KEY, BuildConfig.GOOGLE_KEY)
                    .build()
            ).build()
        )
    }
}