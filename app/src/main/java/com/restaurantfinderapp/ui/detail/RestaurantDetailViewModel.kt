package com.restaurantfinderapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurantfinderapp.data.repository.FindRestaurantRepository
import com.restaurantfinderapp.data.model.RestaurantDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// use dependency injection
class RestaurantDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RestaurantDetailResponse?>(null)
    val uiState : StateFlow<RestaurantDetailResponse?> = _uiState

    private val _showProgress = MutableStateFlow<Boolean>(true)
    val showProgress : StateFlow<Boolean> = _showProgress

    fun getRestaurantDetail(repository: FindRestaurantRepository, fsqId: String) {
        _showProgress.value = true
        viewModelScope.launch {
            val response = repository.getRestaurantDetail(fsqId.replace("{", "").replace("}", ""))
            if (response.isSuccessful) {
                _showProgress.value = false
                _uiState.value = response.body()
            }
        }
    }

    companion object {
        const val FSQ_ID_KEY = "fsqId"
    }
}