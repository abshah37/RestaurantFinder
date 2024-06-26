package com.restaurantfinderapp.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.restaurantfinderapp.ui.detail.RestaurantDetailScreen
import com.restaurantfinderapp.ui.home.HomeScreen
import com.restaurantfinderapp.ui.detail.RestaurantDetailViewModel

@Composable
fun RestaurantApp() {
    val navController = rememberNavController()
    RestaurantNavHost(
        navController = navController
    )
}

@Composable
fun RestaurantNavHost(
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onRestaurantItemClick = {
                    navController.navigate(
                        Screen.RestaurantDetail.createRoute(
                            fsqId = it.fsq_id.orEmpty()
                        )
                    )
                }
            )
        }
        composable(
            route = Screen.RestaurantDetail.route,
            arguments = Screen.RestaurantDetail.navArguments
        ) { backStackEntry ->
            val fsqId = backStackEntry.arguments?.getString(RestaurantDetailViewModel.FSQ_ID_KEY).orEmpty()
            RestaurantDetailScreen(
                fsqId = fsqId,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
