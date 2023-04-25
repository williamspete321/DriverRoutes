package com.williams.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalDriverRouteCrossRef
import com.williams.data.source.local.model.LocalDriverWithRoutes
import com.williams.data.source.local.model.LocalRoute
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DriverRouteDao {

    @Query("SELECT * FROM LocalDriver")
    abstract fun getDrivers(): Flow<List<LocalDriver>>

    @Transaction
    @Query("SELECT * FROM LocalDriver WHERE driverId = :id")
    abstract fun getDriverWithRoutes(id: Int): LocalDriverWithRoutes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertLocalDriverWithRoutes(
        driver: LocalDriver,
        routes: List<LocalRoute>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDriverRoutesCrossRef(driverWithRoutes: LocalDriverRouteCrossRef)

    suspend fun insertLocalDriversWithRoutes(
        driversWithRoutes: List<LocalDriverWithRoutes>
    ) {
        for (driverWithRoute in driversWithRoutes) {
            insertLocalDriverWithRoutes(
                driverWithRoute.driver,
                driverWithRoute.routes
            )

            for (route in driverWithRoute.routes) {
                insertDriverRoutesCrossRef(
                    LocalDriverRouteCrossRef(
                        driverWithRoute.driver.driverId,
                        route.routeId
                    )
                )
            }
        }
    }
}