package com.arjun.samachar.data.model

interface HeadlineContract {
    val author: String
    val title: String
    val description: String
    val url: String
    val imageUrl: String
    val publishedAt: String
    val source: SourceContract
}