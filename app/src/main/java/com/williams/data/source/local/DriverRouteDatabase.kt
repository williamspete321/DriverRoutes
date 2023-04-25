package com.williams.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalDriverRouteCrossRef
import com.williams.data.source.local.model.LocalRoute

@Database(
    entities = [
        LocalDriver::class,
        LocalRoute::class,
        LocalDriverRouteCrossRef::class
    ],
    version = 1
)
abstract class DriverRouteDatabase : RoomDatabase() {
    abstract fun driverRouteDao(): DriverRouteDao
}