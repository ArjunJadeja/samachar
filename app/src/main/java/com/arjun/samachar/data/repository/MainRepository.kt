package com.arjun.samachar.data.repository

import com.arjun.samachar.data.api.NetworkService
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.model.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val networkService: NetworkService) {

    fun getHeadlinesByCountry(countryCode: String): Flow<List<Headline>> {
        return flow { emit(networkService.getHeadlinesByCountry(countryCode = countryCode)) }
            .map { it.headlines }
    }

    fun search(query: String): Flow<List<Headline>> {
        return flow { emit(networkService.search(query = query)) }.map { it.headlines }
    }

    fun getHeadlinesBySource(sourceId: String): Flow<List<Headline>> {
        return flow { emit(networkService.getHeadlinesBySource(sourceId = sourceId)) }.map { it.headlines }
    }

    fun getHeadlinesByLanguage(countryCode: String, languageCode: String): Flow<List<Headline>> {
        return flow {
            emit(
                networkService.getHeadlinesByLanguage(
                    countryCode = countryCode,
                    languageCode = languageCode
                )
            )
        }.map { it.headlines }
    }

    fun getSources(countryCode: String): Flow<List<Source>> {
        return flow { emit(networkService.getSources(countryCode = countryCode)) }
            .map { it.sources }
    }

}