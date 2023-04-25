package com.williams.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.williams.data.Driver

@Entity
data class LocalDriver(
    @PrimaryKey val driverId: Long = 0L,
    val name: String
)

fun LocalDriver.toDomainDriver(): Driver {
    val firstLastName = this.name.splitName()
    return Driver(
        id = driverId.toInt(),
        firstName = firstLastName?.first.orEmpty(),
        lastName = firstLastName?.second.orEmpty(),
        routes = emptyList()
    )
}

fun String.splitName(): Pair<String, String>? {
    val names = this.split(" ")
    var firstLastName: Pair<String, String>? = null
    if (names.size >= 2) {
        firstLastName = Pair(names[0], names[names.size-1])
    }
    return firstLastName
}
