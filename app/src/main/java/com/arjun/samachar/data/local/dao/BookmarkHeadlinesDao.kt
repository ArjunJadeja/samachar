package com.arjun.samachar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arjun.samachar.data.local.entity.BookmarkHeadline
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkHeadlinesDao {

    @Query("SELECT * FROM saved_headlines")
    fun getAll(): Flow<List<BookmarkHeadline>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(headline: BookmarkHeadline)

    @Delete
    fun remove(headline: BookmarkHeadline)

}