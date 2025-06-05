package com.kombucha.dailyhabitat.data.datasource

import com.kombucha.dailyhabitat.data.network.entity.HabitatResponse
import kotlinx.coroutines.flow.Flow

interface HabitatRemoteDataSource {

    fun getHabitatList(): Flow<List<HabitatResponse>>
}