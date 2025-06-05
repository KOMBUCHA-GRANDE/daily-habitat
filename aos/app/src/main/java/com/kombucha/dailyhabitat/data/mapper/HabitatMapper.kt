package com.kombucha.dailyhabitat.data.mapper

import com.kombucha.dailyhabitat.data.network.entity.HabitatResponse
import com.kombucha.dailyhabitat.domain.model.Habitat

object HabitatMapper {

    fun responseToData(habitatList: List<HabitatResponse>) : List<Habitat> {
        return habitatList.map { habitatResponse ->
            Habitat(
                id = habitatResponse.id,
                name = habitatResponse.name,
                emoji = habitatResponse.emoji,
                frequency = habitatResponse.frequency,
                backgroundColor = habitatResponse.backgroundColor,
                stackCount = habitatResponse.stackCount,
                startDate = habitatResponse.startDate,
                stackStartDate = habitatResponse.stackStartDate,
                nextScheduledDate = habitatResponse.nextScheduledDate,
                createdAt = habitatResponse.createdAt
            )
        }
    }
}