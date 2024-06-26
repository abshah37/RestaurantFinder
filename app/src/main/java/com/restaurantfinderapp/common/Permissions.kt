package com.restaurantfinderapp.common

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestComposablePermission(
    permission: String,
    onDenied: @Composable () -> Unit,
    onGranted: @Composable () -> Unit,
    onPermissionDenied: () -> Unit
) {

    var grantState by remember {
        mutableStateOf<Boolean?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        grantState = isGranted
        if (!isGranted) {
            onPermissionDenied()
        }
    }

    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            permission
        ) -> {
            grantState = true
        }
        else -> {
            SideEffect {
                launcher.launch(permission)
            }
        }
    }

    if (grantState == true) {
        onGranted()
    } else {
        onDenied()
    }
}
