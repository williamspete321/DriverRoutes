package com.williams.data

data class Driver(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val routes: List<Route>
)
