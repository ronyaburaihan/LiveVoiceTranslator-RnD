package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(
    controller: CameraController,
    modifier: Modifier
) {
    val session = controller.session

    UIKitViewController(
        factory = {
            val viewController = UIViewController()
            val containerView = UIView(frame = CGRectMake(0.0, 0.0, 1.0, 1.0))

            val previewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
                videoGravity = AVLayerVideoGravityResizeAspectFill
            }

            containerView.layer.addSublayer(previewLayer)
            viewController.setView(containerView)

            dispatch_async(dispatch_get_main_queue()) {
                previewLayer.setFrame(containerView.bounds)
            }

            viewController
        },
        modifier = modifier,
        update = { viewController ->
            // Update preview layer frame when view bounds change
            val containerView = viewController.view
            val previewLayer =
                containerView.layer.sublayers?.firstOrNull() as? AVCaptureVideoPreviewLayer
            previewLayer?.setFrame(containerView.bounds)
        },
        onRelease = {
            // Session is managed by CameraController
        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}
