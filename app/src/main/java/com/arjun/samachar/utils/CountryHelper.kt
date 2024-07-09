package com.arjun.samachar.utils

import com.arjun.samachar.data.model.CountriesResponse
import com.arjun.samachar.data.model.Country

object CountryHelper {

    private val countries = listOf(
        Country("AR", "Argentina", "🇦🇷"),
        Country("AU", "Australia", "🇦🇺"),
        Country("AT", "Austria", "🇦🇹"),
        Country("BE", "Belgium", "🇧🇪"),
        Country("BR", "Brazil", "🇧🇷"),
        Country("BG", "Bulgaria", "🇧🇬"),
        Country("CA", "Canada", "🇨🇦"),
        Country("CN", "China", "🇨🇳"),
        Country("CO", "Colombia", "🇨🇴"),
        Country("CU", "Cuba", "🇨🇺"),
        Country("CZ", "Czech Republic", "🇨🇿"),
        Country("EG", "Egypt", "🇪🇬"),
        Country("FR", "France", "🇫🇷"),
        Country("DE", "Germany", "🇩🇪"),
        Country("GR", "Greece", "🇬🇷"),
        Country("HK", "Hong Kong", "🇭🇰"),
        Country("HU", "Hungary", "🇭🇺"),
        Country("IN", "India", "🇮🇳"),
        Country("ID", "Indonesia", "🇮🇩"),
        Country("IE", "Ireland", "🇮🇪"),
        Country("IL", "Israel", "🇮🇱"),
        Country("IT", "Italy", "🇮🇹"),
        Country("JP", "Japan", "🇯🇵"),
        Country("LV", "Latvia", "🇱🇻"),
        Country("LT", "Lithuania", "🇱🇹"),
        Country("MY", "Malaysia", "🇲🇾"),
        Country("MA", "Morocco", "🇲🇦"),
        Country("MX", "Mexico", "🇲🇽"),
        Country("NL", "Netherlands", "🇳🇱"),
        Country("NZ", "New Zealand", "🇳🇿"),
        Country("NG", "Nigeria", "🇳🇬"),
        Country("NO", "Norway", "🇳🇴"),
        Country("PH", "Philippines", "🇵🇭"),
        Country("PL", "Poland", "🇵🇱"),
        Country("PT", "Portugal", "🇵🇹"),
        Country("RO", "Romania", "🇷🇴"),
        Country("RU", "Russia", "🇷🇺"),
        Country("SA", "Saudi Arabia", "🇸🇦"),
        Country("RS", "Serbia", "🇷🇸"),
        Country("SG", "Singapore", "🇸🇬"),
        Country("SK", "Slovakia", "🇸🇰"),
        Country("SI", "Slovenia", "🇸🇮"),
        Country("ZA", "South Africa", "🇿🇦"),
        Country("KR", "South Korea", "🇰🇷"),
        Country("ES", "Spain", "🇪🇸"),
        Country("SE", "Sweden", "🇸🇪"),
        Country("CH", "Switzerland", "🇨🇭"),
        Country("TW", "Taiwan", "🇹🇼"),
        Country("TH", "Thailand", "🇹🇭"),
        Country("TR", "Turkey", "🇹🇷"),
        Country("UA", "Ukraine", "🇺🇦"),
        Country("AE", "United Arab Emirates", "🇦🇪"),
        Country("GB", "United Kingdom", "🇬🇧"),
        Country("US", "United States", "🇺🇸"),
        Country("VE", "Venezuela", "🇻🇪")
    )

    fun getCountries(): CountriesResponse {
        return CountriesResponse(
            pageSize = countries.size,
            pageNo = 1,
            totalItems = countries.size,
            countries = countries
        )
    }

    fun getCountries(pageSize: Int, pageNo: Int): CountriesResponse {
        val totalItems = countries.size
        val fromIndex = pageSize * (pageNo - 1)
        if (fromIndex >= totalItems) {
            return CountriesResponse(pageSize, pageNo, totalItems, emptyList())
        }
        val toIndex = minOf(fromIndex + pageSize, totalItems)
        val paginatedCountries = countries.subList(fromIndex, toIndex)
        return CountriesResponse(pageSize, pageNo, totalItems, paginatedCountries)
    }

}