package com.arjun.samachar.data.model

import com.arjun.samachar.utils.AppConstants.DEFAULT_SOURCE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("id")
    val id: String = DEFAULT_SOURCE,
    @SerialName("name")
    val name: String = DEFAULT_SOURCE,
)