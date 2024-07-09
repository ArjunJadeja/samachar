package com.arjun.samachar.data.api

import com.arjun.samachar.data.model.HeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET("top-headlines")
    suspend fun getHeadlinesByCountry(
        @Query("country") countryCode: String
    ): HeadlinesResponse

    @GET("everything")
    suspend fun search(
        @Query("q") query: String
    ): HeadlinesResponse

}