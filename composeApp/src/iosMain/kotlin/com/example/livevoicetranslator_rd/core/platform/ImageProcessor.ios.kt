package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage

actual class ImageProcessor {
    actual suspend fun rotate(
        image: CameraImage,
        degrees: Int
    ): Result<CameraImage> {
        TODO("Not yet implemented")
    }

    actual suspend fun crop(
        image: CameraImage,
        left: Int,
        top: Int,
        width: Int,
        height: Int
    ): Result<CameraImage> {
        TODO("Not yet implemented")
    }

    actual suspend fun detectEdges(image: CameraImage): Result<List<Point>> {
        TODO("Not yet implemented")
    }

    actual suspend fun adjustBrightness(
        image: CameraImage,
        factor: Float
    ): Result<CameraImage> {
        TODO("Not yet implemented")
    }
}