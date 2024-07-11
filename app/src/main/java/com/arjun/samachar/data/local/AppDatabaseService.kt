package com.arjun.samachar.data.local

import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.local.entity.toHeadline
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.remote.model.toBookmarkHeadline
import com.arjun.samachar.data.remote.model.toCacheHeadline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDatabaseService @Inject constructor(private val appDatabase: AppDatabase) :
    DatabaseService {

    override fun getCachedHeadlines(): Flow<List<Headline>> {
        return appDatabase.cacheHeadlinesDao().getAll().map { headlineCacheList ->
            headlineCacheList.map { it.toHeadline() }
        }
    }

    override fun deleteAllAndInsertAllToCache(headlines: List<Headline>) {
        val headlineCacheList = headlines.map { it.toCacheHeadline() }
        appDatabase.cacheHeadlinesDao().deleteAllAndInsertAll(headlineCacheList)
    }

    override fun cacheAll(headlines: List<Headline>) {
        val headlineCacheList = headlines.map { it.toCacheHeadline() }
        appDatabase.cacheHeadlinesDao().addAll(headlineCacheList)
    }

    override fun getBookmarkedHeadlines(): Flow<List<BookmarkHeadline>> {
        return appDatabase.bookmarkHeadlinesDao().getAll()
    }

    override fun bookmarkHeadline(headline: Headline) {
        appDatabase.bookmarkHeadlinesDao().add(headline = headline.toBookmarkHeadline())
    }

    override fun removeFromBookmarkedHeadlines(headline: BookmarkHeadline) {
        appDatabase.bookmarkHeadlinesDao().remove(headline = headline)
    }

}