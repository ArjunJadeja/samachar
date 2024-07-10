package com.arjun.samachar.data.remote

import com.arjun.samachar.utils.AppConstants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("X-Api-Key", API_KEY)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}