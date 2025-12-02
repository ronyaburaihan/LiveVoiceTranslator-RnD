package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage

expect class ImageProcessor {
    suspend fun rotate(image: CameraImage, degrees: Int): Result<CameraImage>
    suspend fun crop(
        image: CameraImage,
        left: Int,
        top: Int,
        width: Int,
        height: Int
    ): Result<CameraImage>
    suspend fun detectEdges(image: CameraImage): Result<List<Point>>
    suspend fun adjustBrightness(image: CameraImage, factor: Float): Result<CameraImage>
}

data class Point(val x: Float, val y: Float)