package com.arjun.samachar.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourcesResponse (
    @SerialName("status")
    val status: String? = null,
    @SerialName("sources")
    val sources: List<Source> = ArrayList(),
)