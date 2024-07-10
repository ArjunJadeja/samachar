package com.arjun.samachar.data.repository

import com.arjun.samachar.data.api.NetworkService
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.data.model.Source
import com.arjun.samachar.utils.CountryHelper
import com.arjun.samachar.utils.LanguageHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val networkService: NetworkService) {

    fun getAllCountries(): Flow<List<Country>> {
        return flow { emit(CountryHelper.getCountries()) }.map { it.countries }
    }

    fun getAllLanguages(): Flow<List<Language>> {
        return flow { emit(LanguageHelper.getAllLanguages()) }
    }

    fun getHeadlinesByCountry(countryCode: String): Flow<List<Headline>> {
        return flow { emit(networkService.getHeadlinesByCountry(countryCode = countryCode).headlines) }
            .map { headlines ->
                headlines.filter { it.title.uppercase() != "[REMOVED]" }
            }
    }

    fun search(query: String): Flow<List<Headline>> {
        return flow { emit(networkService.search(query = query).headlines) }
            .map { headlines ->
                headlines.filter { it.title.uppercase() != "[REMOVED]" }
            }
    }

    fun getHeadlinesBySource(sourceId: String): Flow<List<Headline>> {
        return flow { emit(networkService.getHeadlinesBySource(sourceId = sourceId).headlines) }
            .map { headlines ->
                headlines.filter { it.title.uppercase() != "[REMOVED]" }
            }
    }

    fun getHeadlinesByLanguage(countryCode: String, languageCode: String): Flow<List<Headline>> {
        return flow {
            emit(
                networkService.getHeadlinesByLanguage(
                    countryCode = countryCode,
                    languageCode = languageCode
                ).headlines
            )
        }.map { headlines ->
            headlines.filter { it.title.uppercase() != "[REMOVED]" && it.imageUrl.isNotEmpty() }
        }
    }

    fun getSources(countryCode: String): Flow<List<Source>> {
        return flow { emit(networkService.getSources(countryCode = countryCode)) }
            .map { it.sources }
    }

}