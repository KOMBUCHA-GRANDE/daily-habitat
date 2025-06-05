package com.kombucha.dailyhabitat.domain.model

data class Habitat(
    val id: Int,
    val name: String,
    val emoji: Int,
    val frequency: String,
    val backgroundColor: String,
    val stackCount: Int,
    val startDate: String,
    val stackStartDate: String,
    val nextScheduledDate: String,
    val createdAt: String,
)
