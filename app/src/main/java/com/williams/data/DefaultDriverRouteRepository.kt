package com.williams.data

import com.williams.data.source.local.LocalDataSource
import com.williams.data.source.local.model.toDomainDriver
import com.williams.data.source.local.model.toDomainModel
import com.williams.data.source.network.NetworkDataSource
import com.williams.data.source.network.model.toLocalModel
import com.williams.data.source.network.model.toLocalRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DefaultDriverRouteRepository(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : DriverRouteRepository {

    override suspend fun getDrivers(): Flow<List<Driver>> {
        return localDataSource
            .getDrivers()
            .distinctUntilChanged()
            .map { driverList->
                driverList.map {
                    it.toDomainDriver()
                }
            }
    }

    override suspend fun refreshDriverRoutes() {
        val driverRouteContainer = networkDataSource.getDriverRouteContainer()

        val drivers = driverRouteContainer.drivers?.map { it.toLocalModel() } ?: emptyList()
        val routes = driverRouteContainer.routes?.map { it.toLocalRoute() } ?: emptyList()

        val driversWithRoutes = RouteAssigner.assign(drivers, routes)
        localDataSource.insertDriversWithRoutes(driversWithRoutes)
    }

    override suspend fun getDriverWithRoutes(driverId: Int): Driver {
        return localDataSource
            .getDriverWithRoutes(driverId)
            .toDomainModel()
    }

}