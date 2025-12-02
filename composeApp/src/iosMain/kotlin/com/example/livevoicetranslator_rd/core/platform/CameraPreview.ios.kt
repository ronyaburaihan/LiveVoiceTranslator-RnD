package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.UIKit.UIView
import platform.QuartzCore.CATransaction

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(
    controller: CameraController,
    modifier: Modifier
) {
    val session = controller.session
    
    UIKitView(
        factory = {
            val view = UIView()
            val previewLayer = AVCaptureVideoPreviewLayer(session = session)
            previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            view.layer.addSublayer(previewLayer)
            view
        },
        modifier = modifier,
        update = { view ->
            val layer = view.layer.sublayers?.firstOrNull() as? AVCaptureVideoPreviewLayer
            layer?.frame = view.bounds
            // Ensure animations are disabled for frame updates to avoid glitches
            CATransaction.begin()
            CATransaction.setDisableActions(true)
            layer?.frame = view.bounds
            CATransaction.commit()
        }
    )
}
