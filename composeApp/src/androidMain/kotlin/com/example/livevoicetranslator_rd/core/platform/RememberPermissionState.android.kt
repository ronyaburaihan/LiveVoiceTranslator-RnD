package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.pm.PackageManager
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun rememberPermissionState(permission: Permission): PermissionState {
    val context = LocalContext.current
    var status by remember { mutableStateOf(PermissionStatus.NOT_DETERMINED) }

    val permissionString = when (permission) {
        Permission.CAMERA -> android.Manifest.permission.CAMERA
        Permission.PHOTO_LIBRARY -> android.Manifest.permission.READ_EXTERNAL_STORAGE
        Permission.MICROPHONE -> android.Manifest.permission.RECORD_AUDIO
    }

    fun checkPermission() {
        status = if (ContextCompat.checkSelfPermission(context, permissionString) == PackageManager.PERMISSION_GRANTED) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        status = if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED
    }

    LaunchedEffect(permission) {
        checkPermission()
    }

    return remember(status, launcher) {
        object : PermissionState {
            override val status: PermissionStatus = status
            override val isGranted: Boolean = status == PermissionStatus.GRANTED
            override fun launchPermissionRequest() {
                launcher.launch(permissionString)
            }
        }
    }
}
