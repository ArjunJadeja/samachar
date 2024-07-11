package com.arjun.samachar.data.model

sealed class HeadlineQuery {
    data class ByCountry(val countryCode: String) : HeadlineQuery()
    data class BySource(val sourceId: String) : HeadlineQuery()
    data class ByLanguage(val countryCode: String, val languageCode: String) : HeadlineQuery()
    data class BySearch(val query: String) : HeadlineQuery()
}