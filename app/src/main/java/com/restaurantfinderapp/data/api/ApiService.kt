package com.restaurantfinderapp.data.api

import com.restaurantfinderapp.data.repository.FindRestaurantRepository.Companion.NETWORK_PAGE_SIZE
import com.restaurantfinderapp.data.model.GetRestaurantsResponse
import com.restaurantfinderapp.data.model.RestaurantDetailResponse
import com.restaurantfinderapp.utils.ApiConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("places/search")
    suspend fun searchRestaurants(
        @Query("fields") fields: String = "fsq_id,name,photos,hours,distance,link,rating",
        @Query("query") query: String = "Restaurant",
        @Query("limit") limit: Int = NETWORK_PAGE_SIZE,
        @Query("sort") sort: String = "DISTANCE",
        @Query("cursor") cursor: String?,
        @Query("ll") ll: String?,
        @Header("Authorization") authorization: String = ApiConstants.FOURSQUARE_API_KEY
    ): Response<GetRestaurantsResponse>

    @GET("places/{fsq_id}")
    suspend fun restaurantDetail(
        @Path("fsq_id") fsqId: String,
        @Query("fields") fields: String = "fsq_id,name,geocodes,rating,photos,location,hours,distance,link,stats",
        @Header("Authorization") authorization: String = ApiConstants.FOURSQUARE_API_KEY // use env variable
    ): Response<RestaurantDetailResponse>

    companion object {
        private const val BASE_URL = "https://api.foursquare.com/v3/" // use env variable

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}