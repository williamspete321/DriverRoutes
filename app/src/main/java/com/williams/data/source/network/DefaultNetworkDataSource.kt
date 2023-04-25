package com.williams.data.source.network

import com.williams.data.source.network.model.ApiDriverRouteContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultNetworkDataSource(
    private val apiService: DriverRouteApi
) : NetworkDataSource {

    override suspend fun getDriverRouteContainer(): ApiDriverRouteContainer =
        withContext(Dispatchers.IO) {
            apiService.getDriverRoute()
        }

}