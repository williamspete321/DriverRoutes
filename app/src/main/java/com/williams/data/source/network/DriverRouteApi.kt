package com.williams.data.source.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.williams.data.source.network.NetworkConstants.BASE_ENDPOINT
import com.williams.data.source.network.NetworkConstants.DRIVER_ROUTE_ENDPOINT
import com.williams.data.source.network.model.ApiDriverRouteContainer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface DriverRouteApi {
    @GET(DRIVER_ROUTE_ENDPOINT)
    suspend fun getDriverRoute(): ApiDriverRouteContainer
}

object NetworkConstants {
    const val BASE_ENDPOINT = "https://d49c3a78-a4f2-437d-bf72-569334dea17c.mock.pstmn.io/"
    const val DRIVER_ROUTE_ENDPOINT = "data"
}

object DriverRouteNetwork {

    private val moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_ENDPOINT)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val driverRoute = retrofit.create(DriverRouteApi::class.java)
}