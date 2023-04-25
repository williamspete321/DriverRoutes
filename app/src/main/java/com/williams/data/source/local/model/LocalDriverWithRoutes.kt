package com.williams.data.source.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.williams.data.Driver
import com.williams.data.source.local.model.LocalDriverRouteCrossRef

data class LocalDriverWithRoutes(
    @Embedded
    val driver: LocalDriver,
    @Relation(
        parentColumn = "driverId",
        entity = LocalRoute::class,
        entityColumn = "routeId",
        associateBy = Junction(
            value = LocalDriverRouteCrossRef::class,
            parentColumn = "driverId",
            entityColumn = "routeId"
        )
    )
    val routes: List<LocalRoute>
)

fun LocalDriverWithRoutes.toDomainModel(): Driver {
    val firstLastName = driver.name.splitName()
    return Driver(
        id = driver.driverId.toInt(),
        firstName = firstLastName?.first.orEmpty(),
        lastName = firstLastName?.second.orEmpty(),
        routes = routes.map { it.toDomainRoute() }
    )
}
