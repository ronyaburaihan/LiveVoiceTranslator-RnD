package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

enum class PermissionStatus {
    GRANTED,
    DENIED,
    NOT_DETERMINED
}

@Composable
expect fun rememberPermissionState(permission: Permission): PermissionState

interface PermissionState {
    val status: PermissionStatus
    val isGranted: Boolean
    fun launchPermissionRequest()
}
