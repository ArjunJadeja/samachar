package com.arjun.samachar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arjun.samachar.data.local.dao.CacheHeadlinesDao
import com.arjun.samachar.data.local.dao.BookmarkHeadlinesDao
import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.local.entity.CacheHeadline

@Database(
    entities = [BookmarkHeadline::class, CacheHeadline::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkHeadlinesDao(): BookmarkHeadlinesDao
    abstract fun cacheHeadlinesDao(): CacheHeadlinesDao
}