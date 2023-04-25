package com.williams.ui.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williams.data.DriverRouteRepository
import com.williams.data.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DriverRoutesViewModel(
    private val driverId: Int,
    private val repository: DriverRouteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RoutesUiState> =
        MutableStateFlow(RoutesUiState.Success(emptyList()))
    val uiState: StateFlow<RoutesUiState> = _uiState

    init {
        getDriversWithRoutes()
        collectUiState()
    }

    private fun getDriversWithRoutes() {
        viewModelScope.launch {
            repository.getDriverWithRoutes(driverId)
        }
    }

    private fun collectUiState() {
        viewModelScope.launch {
            try {
                _uiState.value = RoutesUiState.Loading(true)
                val driversWithRoutes = repository.getDriverWithRoutes(driverId)
                _uiState.value = RoutesUiState.Loading(false)
                _uiState.value = RoutesUiState.Success(driversWithRoutes.routes)
            } catch (e: Exception) {
                _uiState.value = RoutesUiState.Exception(e)
            }
        }
    }

}

sealed class RoutesUiState {
    data class Success(val drivers: List<Route>): RoutesUiState()
    data class Exception(val exception: Throwable): RoutesUiState()
    data class Loading(val loading: Boolean): RoutesUiState()
}