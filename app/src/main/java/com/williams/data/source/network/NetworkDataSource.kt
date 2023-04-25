package com.williams.data.source.network

import com.williams.data.source.network.model.ApiDriverRouteContainer

interface NetworkDataSource {
    suspend fun getDriverRouteContainer(): ApiDriverRouteContainer
}