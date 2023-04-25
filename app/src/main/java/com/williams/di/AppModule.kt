package com.williams.di

import androidx.room.Room
import com.williams.data.DefaultDriverRouteRepository
import com.williams.data.DriverRouteRepository
import com.williams.data.source.local.DriverRouteDao
import com.williams.data.source.local.DriverRouteDatabase
import com.williams.data.source.local.LocalDataSource
import com.williams.data.source.local.RoomLocalDataSource
import com.williams.data.source.network.DefaultNetworkDataSource
import com.williams.data.source.network.DriverRouteNetwork
import com.williams.data.source.network.NetworkDataSource
import com.williams.ui.drivers.DriversViewModel
import com.williams.ui.routes.DriverRoutesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<NetworkDataSource> {
        DefaultNetworkDataSource(
            DriverRouteNetwork.driverRoute
        )
    }

    single<DriverRouteDatabase> {
        Room.databaseBuilder(
            androidContext(),
            DriverRouteDatabase::class.java,
            "DriverRoute.cb"
        ).build()
    }

    single<DriverRouteDao> {
        val database = get<DriverRouteDatabase>()
        database.driverRouteDao()
    }

    single<LocalDataSource> {
        RoomLocalDataSource(
            get<DriverRouteDao>()
        )
    }

    single<DriverRouteRepository> {
        DefaultDriverRouteRepository(
            get<NetworkDataSource>(),
            get<LocalDataSource>()
        )
    }

    viewModel {
        DriversViewModel(
            get<DriverRouteRepository>()
        )
    }

    viewModel {
        params ->
        DriverRoutesViewModel(
            params.get<Int>(),
            get<DriverRouteRepository>()
        )
    }

}