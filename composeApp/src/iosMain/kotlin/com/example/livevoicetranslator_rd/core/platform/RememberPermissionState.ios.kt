package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberPermissionState(permission: Permission): PermissionState {
    var permissionStatus by remember { mutableStateOf(PermissionStatus.DENIED) }
    var permissionRequester by remember { mutableStateOf<(() -> Unit)?>(null) }
    
    LaunchedEffect(Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        permissionStatus = when (status) {
            AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
    }
    
    permissionRequester = {
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            permissionStatus = if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
        }
    }
    
    return remember(permissionStatus, permissionRequester) {
        object : PermissionState {
            override val status: PermissionStatus = permissionStatus
            override val isGranted: Boolean = permissionStatus == PermissionStatus.GRANTED
            override fun launchPermissionRequest() {
                permissionRequester?.invoke()
            }
        }
    }
}
