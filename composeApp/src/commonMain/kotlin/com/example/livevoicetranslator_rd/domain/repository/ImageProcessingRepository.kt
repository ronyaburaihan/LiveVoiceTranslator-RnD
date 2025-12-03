package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.CameraImage

interface ImageProcessingRepository {
    suspend fun autoRotate(image: CameraImage): Result<CameraImage>
    suspend fun autoCrop(image: CameraImage): Result<CameraImage>
    suspend fun adjustBrightness(image: CameraImage, factor: Float): Result<CameraImage>
    fun calculateImageQuality(image: CameraImage): Float
}