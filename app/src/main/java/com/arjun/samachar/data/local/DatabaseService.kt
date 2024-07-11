package com.arjun.samachar.data.local

import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.remote.model.Headline
import kotlinx.coroutines.flow.Flow

interface DatabaseService {

    fun getCachedHeadlines(): Flow<List<Headline>>

    fun deleteAllAndInsertAllToCache(headlines: List<Headline>)

    fun cacheAll(headlines: List<Headline>)

    fun getBookmarkedHeadlines(): Flow<List<BookmarkHeadline>>

    fun bookmarkHeadline(headline: Headline)

    fun removeFromBookmarkedHeadlines(headline: BookmarkHeadline)

}