package com.arjun.samachar.data.remote.model

import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.local.entity.CacheHeadline
import com.arjun.samachar.data.model.HeadlineContract
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Headline(
    @SerialName("author")
    override val author: String = "",
    @SerialName("title")
    override val title: String = "",
    @SerialName("description")
    override val description: String = "",
    @SerialName("url")
    override val url: String = "",
    @SerialName("urlToImage")
    override val imageUrl: String = "",
    @SerialName("publishedAt")
    override val publishedAt: String = "",
    @SerialName("source")
    override val source: Source
) : HeadlineContract

fun Headline.toBookmarkHeadline(): BookmarkHeadline {
    return BookmarkHeadline(
        url = url,
        author = author,
        title = title,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = source.toSourceEntity()
    )
}

fun Headline.toCacheHeadline(): CacheHeadline {
    return CacheHeadline(
        url = url,
        author = author,
        title = title,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = source.toSourceEntity()
    )
}