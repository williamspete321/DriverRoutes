package com.williams.data

import com.williams.data.source.network.model.Type

data class Route(
    val id: Int,
    val type: Type,
    val name: String
)
