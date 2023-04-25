package com.williams.ui.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williams.data.Driver
import com.williams.data.DriverRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DriversViewModel(
    private val repository: DriverRouteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DriversUiState> =
        MutableStateFlow(DriversUiState.Success(emptyList()))
    val uiState: StateFlow<DriversUiState> = _uiState

    init {
        refreshDrivers()
        collectUiState()
    }

    fun onSortByLastNameClicked() {
        val resultsToSort = _uiState.value as? DriversUiState.Success
        resultsToSort ?: return

        _uiState.value = DriversUiState.Loading(true)

        val sortedByLastName = resultsToSort.drivers.sortedBy { it.lastName }

        _uiState.value = DriversUiState.Loading(false)
        _uiState.value = DriversUiState.Success(sortedByLastName)
    }

    private fun refreshDrivers() {
        viewModelScope.launch {
            repository.refreshDriverRoutes()
        }
    }

    private fun collectUiState() {
        viewModelScope.launch {
            _uiState.value = DriversUiState.Loading(true)
            repository
                .getDrivers()
                .catch { _uiState.value = DriversUiState.Exception(it) }
                .collectLatest { drivers ->
                    _uiState.value = DriversUiState.Loading(false)
                    _uiState.value = DriversUiState.Success(drivers)
                }
        }
    }

}

sealed class DriversUiState {
    data class Success(val drivers: List<Driver>): DriversUiState()
    data class Exception(val exception: Throwable): DriversUiState()
    data class Loading(val loading: Boolean): DriversUiState()
}