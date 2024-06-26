package com.restaurantfinderapp.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.restaurantfinderapp.ui.detail.RestaurantDetailViewModel.Companion.FSQ_ID_KEY

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object RestaurantDetail : Screen(
        route = "restaurantDetail/{fsqId}",
        navArguments = listOf(navArgument(FSQ_ID_KEY) {
            type = NavType.StringType
        })
    ) {
        fun createRoute(fsqId: String) = "restaurantDetail/{${fsqId}}"
    }

}