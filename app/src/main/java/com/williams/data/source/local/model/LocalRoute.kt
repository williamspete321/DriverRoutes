package com.williams.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.williams.data.Route
import com.williams.data.source.network.model.Type

@Entity
data class LocalRoute(
    @PrimaryKey val routeId: Long = 0L,
    val type: Type,
    val name: String
)

fun LocalRoute.toDomainRoute() = Route(
    id = routeId.toInt(),
    type = type,
    name = name
)
