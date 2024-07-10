package com.arjun.samachar.di.module

import com.arjun.samachar.App
import com.arjun.samachar.data.api.AuthInterceptor
import com.arjun.samachar.data.api.NetworkService
import com.arjun.samachar.utils.network.NetworkConnected
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: App) {

    @Singleton
    @Provides
    fun providesNetworkConnection(): NetworkConnected {
        return NetworkConnected(application)
    }

    @Singleton
    @Provides
    fun provideNetworkService(): NetworkService {
        val baseUrl = "https://newsapi.org/v2/"

        val authInterceptor = AuthInterceptor()

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NetworkService::class.java)
    }

}