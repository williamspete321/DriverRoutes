package com.williams.data.source.local

import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalDriverWithRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RoomLocalDataSource(
    private val driverRouteDao: DriverRouteDao
) : LocalDataSource {

    override suspend fun getDrivers(): Flow<List<LocalDriver>> {
        return withContext(Dispatchers.IO) {
            driverRouteDao.getDrivers()
        }
    }

    override suspend fun getDriverWithRoutes(driverId: Int): LocalDriverWithRoutes {
        return withContext(Dispatchers.IO) {
            driverRouteDao.getDriverWithRoutes(driverId)
        }
    }

    override suspend fun insertDriversWithRoutes(driversWithRoutes: List<LocalDriverWithRoutes>) {
        return withContext(Dispatchers.IO) {
            driverRouteDao.insertLocalDriversWithRoutes(driversWithRoutes)
        }
    }

}