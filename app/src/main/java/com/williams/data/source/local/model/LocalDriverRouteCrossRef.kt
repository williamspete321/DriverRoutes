package com.williams.data.source.local.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["driverId", "routeId"]
)
data class LocalDriverRouteCrossRef(
    val driverId: Long,
    val routeId: Long
)