package com.arjun.samachar.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeadlinesResponse(
    @SerialName("status")
    val status: String? = null,
    @SerialName("totalResults")
    val totalResults: Int? = null,
    @SerialName("articles")
    val headlines: List<Headline> = ArrayList(),
)

@Serializable
data class Headline(
    @SerialName("author")
    val author: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("urlToImage")
    val imageUrl: String? = null,
    @SerialName("publishedAt")
    val publishedAt: String? = null,
    @SerialName("source")
    val source: Source? = null
)