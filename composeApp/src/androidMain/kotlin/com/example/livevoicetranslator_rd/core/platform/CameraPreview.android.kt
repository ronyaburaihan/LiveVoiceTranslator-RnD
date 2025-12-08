package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.view.PreviewView

@Composable
actual fun CameraPreview(
    controller: CameraController,
    modifier: Modifier
) {
    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FIT_CENTER
                controller.setPreviewView(this)
            }
        },
        modifier = modifier
    )
}
