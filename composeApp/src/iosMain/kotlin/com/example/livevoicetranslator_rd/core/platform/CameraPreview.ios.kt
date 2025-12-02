package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.UIKit.UIView
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(
    controller: CameraController,
    modifier: Modifier
) {
    val session = controller.session
    var previewLayerRef by remember { mutableStateOf<AVCaptureVideoPreviewLayer?>(null) }

    UIKitView(
        factory = {
            val containerView = UIView()
            val previewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
                videoGravity = AVLayerVideoGravityResizeAspectFill
                frame = containerView.bounds // set initial frame
            }
            containerView.layer.addSublayer(previewLayer)
            previewLayerRef = previewLayer
            containerView
        },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            // Update frame to match view bounds
            previewLayerRef?.frame = view.bounds

            // Ensure session is running
            if (!session.running) {
                dispatch_async(dispatch_get_main_queue()) {
                    session.startRunning()
                }
            }
        },
        onRelease = { view ->
            previewLayerRef?.removeFromSuperlayer()
            previewLayerRef = null
        }
    )
}
