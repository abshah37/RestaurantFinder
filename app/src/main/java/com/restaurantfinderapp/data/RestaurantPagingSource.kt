package com.restaurantfinderapp.data

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.restaurantfinderapp.data.api.ApiService
import com.restaurantfinderapp.data.model.GetRestaurantsResponse

class RestaurantPagingSource(
    private val service: ApiService,
    private val ll: String?
) : PagingSource<Int, GetRestaurantsResponse.Results>() {
    private var cursor: String? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetRestaurantsResponse.Results> {
        return try {

            var nextPageNumber = params.key ?: 0
            nextPageNumber++
            val response = service.searchRestaurants(cursor = cursor, ll = ll)
            val photos = response.body()?.results
            val responseHeaders = response.headers()
            cursor = getCursor(responseHeaders["link"])
            val nextKey = response.body()?.let {
                if (cursor == null) {
                    null
                } else {
                    nextPageNumber++
                }
            }

            LoadResult.Page(
                data = photos?.toMutableList() ?: listOf(),
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetRestaurantsResponse.Results>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getCursor(link: String?) : String? {
        link?.let {
            val uri = Uri.parse(link)
            return uri.getQueryParameter("cursor")
        }
        return null
    }
}