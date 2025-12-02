package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberCameraController(): CameraController {
    return remember { CameraController() }
}
