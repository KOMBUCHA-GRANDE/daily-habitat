package com.kombucha.dailyhabitat.domain.repository

import com.kombucha.dailyhabitat.domain.model.Habitat
import kotlinx.coroutines.flow.Flow

interface HabitatRepository {

    fun getHabitatList(): Flow<List<Habitat>>
}