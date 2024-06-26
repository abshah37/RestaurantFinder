package com.restaurantfinderapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.restaurantfinderapp.data.RestaurantPagingSource
import com.restaurantfinderapp.data.api.ApiService
import com.restaurantfinderapp.data.model.GetRestaurantsResponse
import kotlinx.coroutines.flow.Flow

class FindRestaurantRepository(private val apiService: ApiService) {

    fun getRestaurantsStream(ll: String?): Flow<PagingData<GetRestaurantsResponse.Results>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, prefetchDistance = 2),
            pagingSourceFactory = { RestaurantPagingSource(apiService, ll) }
        ).flow
    }

    suspend fun getRestaurantDetail(fsqId: String) = apiService.restaurantDetail(fsqId = fsqId)

    companion object {
        const val NETWORK_PAGE_SIZE = 8
    }
}