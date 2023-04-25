package com.williams.data

import kotlinx.coroutines.flow.Flow

interface DriverRouteRepository {

    suspend fun getDrivers(): Flow<List<Driver>>

    suspend fun refreshDriverRoutes()

    suspend fun getDriverWithRoutes(driverId: Int): Driver

}