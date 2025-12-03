package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.DisposableEffect

@Composable
actual fun rememberCameraController(): CameraController {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val controller = remember(context, lifecycleOwner) {
        CameraController(context, lifecycleOwner)
    }
    
    DisposableEffect(controller) {
        onDispose {
            // Controller release is handled manually or we can do it here
            // controller.release() // suspend function, can't call here easily without scope
        }
    }
    
    return controller
}
