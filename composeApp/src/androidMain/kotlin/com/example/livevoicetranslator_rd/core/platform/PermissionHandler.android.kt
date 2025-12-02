package com.example.livevoicetranslator_rd.core.platform

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PermissionHandler (
    private val activity: ComponentActivity
) {
    actual suspend fun requestPermission(permission: Permission): PermissionStatus {
        val permissionString = when (permission) {
            Permission.CAMERA -> android.Manifest.permission.CAMERA
            Permission.PHOTO_LIBRARY -> android.Manifest.permission.READ_EXTERNAL_STORAGE
            Permission.MICROPHONE -> android.Manifest.permission.RECORD_AUDIO
        }

        return suspendCoroutine { cont ->
            val launcher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                cont.resume(if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED)
            }
            launcher.launch(permissionString)
        }
    }

    actual suspend fun checkPermission(permission: Permission): PermissionStatus {
        val permissionString = when (permission) {
            Permission.CAMERA -> android.Manifest.permission.CAMERA
            Permission.PHOTO_LIBRARY -> android.Manifest.permission.READ_EXTERNAL_STORAGE
            Permission.MICROPHONE -> android.Manifest.permission.RECORD_AUDIO
        }

        return when (ContextCompat.checkSelfPermission(activity, permissionString)) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.GRANTED
            else -> PermissionStatus.DENIED
        }
    }
}