package com.kombucha.dailyhabitat.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitatResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("emoji") val emoji: Int = 0,
    @SerialName("frequency") val frequency: String = "",
    @SerialName("backgroundColor") val backgroundColor: String = "",
    @SerialName("stackCount") val stackCount: Int = 0,
    @SerialName("startDate") val startDate: String = "",
    @SerialName("stackStartDate") val stackStartDate: String = "",
    @SerialName("nextScheduledDate") val nextScheduledDate: String = "",
    @SerialName("createdAt") val createdAt: String = ""
)
