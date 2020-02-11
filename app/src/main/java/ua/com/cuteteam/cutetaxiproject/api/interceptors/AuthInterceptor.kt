package ua.com.cuteteam.cutetaxiproject.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ua.com.cuteteam.cutetaxiproject.BuildConfig

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addQueryParameter("key", BuildConfig.GOOGLE_KEY)
                    .build()
            ).build()
        )
    }
}