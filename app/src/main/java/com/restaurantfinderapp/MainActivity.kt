package com.restaurantfinderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.restaurantfinderapp.compose.RestaurantApp


class MainActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
//        changeStatusBarColor()

        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }

        enableEdgeToEdge()
        setContent {
            RestaurantApp()
        }
    }

    private fun changeStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars
    }

}