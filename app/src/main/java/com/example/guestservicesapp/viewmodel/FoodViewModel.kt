package com.yourpackage.guestservices.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourpackage.guestservices.data.FoodRepository
import com.yourpackage.guestservices.data.FoodResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class FoodUiState {
    object Loading : FoodUiState()
    data class Success(val data: FoodResponseDto) : FoodUiState()
    data class Error(val message: String) : FoodUiState()
}

class FoodViewModel(
    private val repo: FoodRepository = FoodRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<FoodUiState>(FoodUiState.Loading)
    val state: StateFlow<FoodUiState> = _state

    fun loadFood(token: String) {
        _state.value = FoodUiState.Loading
        viewModelScope.launch {
            try {
                val data = repo.fetchFood(token)
                _state.value = FoodUiState.Success(data)
            } catch (e: Exception) {
                _state.value = FoodUiState.Error(e.message ?: "Failed to load food menu")
            }
        }
    }
}