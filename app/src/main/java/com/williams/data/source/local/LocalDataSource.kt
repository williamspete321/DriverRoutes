package com.williams.data.source.local

import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalDriverWithRoutes
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getDrivers(): Flow<List<LocalDriver>>

    suspend fun getDriverWithRoutes(driverId: Int): LocalDriverWithRoutes

    suspend fun insertDriversWithRoutes(driversWithRoutes: List<LocalDriverWithRoutes>)
}