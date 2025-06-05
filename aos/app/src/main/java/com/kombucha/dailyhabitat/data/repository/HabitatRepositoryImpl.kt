package com.kombucha.dailyhabitat.data.repository

import com.kombucha.dailyhabitat.data.datasource.HabitatRemoteDataSource
import com.kombucha.dailyhabitat.data.mapper.HabitatMapper
import com.kombucha.dailyhabitat.domain.model.Habitat
import com.kombucha.dailyhabitat.domain.repository.HabitatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitatRepositoryImpl(
    private val habitatRemoteDataSource: HabitatRemoteDataSource
) : HabitatRepository {

    override fun getHabitatList(): Flow<List<Habitat>> {
        return habitatRemoteDataSource.getHabitatList().map {
            HabitatMapper.responseToData(it)
        }
    }
}