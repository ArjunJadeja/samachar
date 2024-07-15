package com.arjun.samachar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arjun.samachar.data.local.DatabaseService
import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.remote.NetworkService
import com.arjun.samachar.data.model.Country
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.model.Language
import com.arjun.samachar.data.paging.HeadlinesPagingSource
import com.arjun.samachar.data.model.HeadlineQuery
import com.arjun.samachar.data.remote.model.Source
import com.arjun.samachar.utils.AppConstants.PAGE_SIZE
import com.arjun.samachar.utils.CountryHelper
import com.arjun.samachar.utils.DispatcherProvider
import com.arjun.samachar.utils.LanguageHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val dispatcherProvider: DispatcherProvider
) {

    fun getAllCountries(): Flow<List<Country>> {
        return flow { emit(CountryHelper.getCountries()) }.map { it.countries }
    }

    fun getAllLanguages(): Flow<List<Language>> {
        return flow { emit(LanguageHelper.getAllLanguages()) }
    }

    fun getHeadlinesByCountry(countryCode: String): Flow<PagingData<Headline>> {
        return getHeadlines(HeadlineQuery.ByCountry(countryCode = countryCode))
    }

    fun getHeadlinesBySource(sourceId: String): Flow<PagingData<Headline>> {
        return getHeadlines(HeadlineQuery.BySource(sourceId = sourceId))
    }

    fun getHeadlinesByLanguage(
        countryCode: String,
        languageCode: String
    ): Flow<PagingData<Headline>> {
        return getHeadlines(
            HeadlineQuery.ByLanguage(
                countryCode = countryCode,
                languageCode = languageCode
            )
        )
    }

    fun search(query: String): Flow<PagingData<Headline>> {
        return getHeadlines(HeadlineQuery.BySearch(query = query))
    }

    private fun getHeadlines(headlineQuery: HeadlineQuery): Flow<PagingData<Headline>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                HeadlinesPagingSource(
                    networkService = networkService,
                    databaseService = databaseService,
                    dispatcherProvider = dispatcherProvider,
                    query = headlineQuery
                )
            }
        ).flow
    }

    fun getSources(countryCode: String): Flow<List<Source>> {
        return flow { emit(networkService.getSources(countryCode = countryCode)) }
            .map { it.sources }
    }

    suspend fun getCachedHeadlines(): Flow<List<Headline>> {
        return withContext(dispatcherProvider.io) { databaseService.getCachedHeadlines() }
    }

    suspend fun bookmarkHeadline(headline: Headline) {
        withContext(dispatcherProvider.io) { databaseService.bookmarkHeadline(headline = headline) }
    }

    suspend fun removeFromBookmarkedHeadlines(headline: BookmarkHeadline) {
        withContext(dispatcherProvider.io) { databaseService.removeFromBookmarkedHeadlines(headline = headline) }
    }

    suspend fun getBookmarkedHeadlines(): Flow<List<BookmarkHeadline>> {
        return withContext(dispatcherProvider.io) { databaseService.getBookmarkedHeadlines() }
    }

}