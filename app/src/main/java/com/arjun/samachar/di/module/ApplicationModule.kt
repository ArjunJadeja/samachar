package com.arjun.samachar.di.module

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.room.Room
import com.arjun.samachar.data.local.AppDatabase
import com.arjun.samachar.data.local.AppDatabaseService
import com.arjun.samachar.data.local.DatabaseService
import com.arjun.samachar.data.remote.AuthInterceptor
import com.arjun.samachar.data.remote.NetworkService
import com.arjun.samachar.di.ApiKey
import com.arjun.samachar.di.BaseUrl
import com.arjun.samachar.utils.AppConstants.API_KEY
import com.arjun.samachar.utils.DefaultDispatcherProvider
import com.arjun.samachar.utils.DispatcherProvider
import com.arjun.samachar.utils.network.NetworkConnected
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideNetworkConnected(@ApplicationContext context: Context): NetworkConnected {
        return NetworkConnected(context)
    }

    @Singleton
    @Provides
    fun provideCustomTabsIntent(): CustomTabsIntent {
        return CustomTabsIntent.Builder().build()
    }

    @BaseUrl
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return "https://newsapi.org/v2/"
    }

    @ApiKey
    @Singleton
    @Provides
    fun provideApiKey(): String {
        return API_KEY
    }

    @Singleton
    @Provides
    fun provideNetworkService(@BaseUrl baseUrl: String, @ApiKey apiKey: String): NetworkService {

        val authInterceptor = AuthInterceptor(apiKey = apiKey)

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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "samachar_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseService(appDatabase: AppDatabase): DatabaseService {
        return AppDatabaseService(appDatabase)
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

}