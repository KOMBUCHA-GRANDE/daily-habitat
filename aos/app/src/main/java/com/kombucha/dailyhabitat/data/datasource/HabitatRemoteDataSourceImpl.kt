package com.kombucha.dailyhabitat.data.datasource

import com.kombucha.dailyhabitat.data.network.entity.HabitatResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HabitatRemoteDataSourceImpl : HabitatRemoteDataSource {

    override fun getHabitatList(): Flow<List<HabitatResponse>> {
        return flow {
            emit(
                listOf(
                    HabitatResponse()
                )
            )
        }
    }
}