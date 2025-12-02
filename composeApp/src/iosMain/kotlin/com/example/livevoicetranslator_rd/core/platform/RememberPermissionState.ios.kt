package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPermissionState(permission: Permission): PermissionState {
    return remember {
        object : PermissionState {
            override val status: PermissionStatus = PermissionStatus.GRANTED // Stub
            override val isGranted: Boolean = true
            override fun launchPermissionRequest() {
                // Stub
            }
        }
    }
}
