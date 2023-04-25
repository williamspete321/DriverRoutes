package com.williams.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalRoute

@JsonClass(generateAdapter = true)
data class ApiDriverRouteContainer(
    @field:Json(name = "drivers") val drivers: List<ApiDriver>?,
    @field:Json(name = "routes") val routes: List<ApiRoute>?
)

@JsonClass(generateAdapter = true)
data class ApiDriver(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class ApiRoute(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "type") val type: Type?,
    @field:Json(name = "name") val name: String?
)

enum class Type {
    R, C, I
}

fun ApiDriver.toLocalModel() = LocalDriver(
    driverId = id?.toLong() ?: 0L,
    name = name.orEmpty()
)

fun ApiRoute.toLocalRoute() = LocalRoute(
    routeId = id?.toLong() ?: 0L,
    type = type ?: Type.I,
    name = name.orEmpty()
)