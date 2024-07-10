package com.arjun.samachar.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arjun.samachar.data.model.HeadlineContract
import com.arjun.samachar.data.remote.model.Headline

@Entity(
    tableName = "saved_headlines",
    indices = [Index(value = ["url", "publishedAt"], unique = true)]
)
data class BookmarkHeadline(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "url")
    override val url: String,
    @ColumnInfo(name = "author")
    override val author: String,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "description")
    override val description: String,
    @ColumnInfo(name = "imageUrl")
    override val imageUrl: String,
    @ColumnInfo(name = "publishedAt")
    override val publishedAt: String,
    @Embedded override val source: SourceEntity
) : HeadlineContract
