package com.restaurantfinderapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.restaurantfinderapp.data.repository.FindRestaurantRepository
import com.restaurantfinderapp.data.model.GetRestaurantsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _restaurants = MutableStateFlow<PagingData<GetRestaurantsResponse.Results>?>(null)
    val restaurants: Flow<PagingData<GetRestaurantsResponse.Results>> get() = _restaurants.filterNotNull()

    fun getRestaurants(repository: FindRestaurantRepository, ll: String?) {
        viewModelScope.launch {
            try {
                _restaurants.value =
                    repository.getRestaurantsStream(ll).cachedIn(viewModelScope).first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}