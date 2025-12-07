package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.data.mapper.mapToOCRResult
import com.example.livevoicetranslator_rd.data.mapper.toVisionApiRequest
import com.example.livevoicetranslator_rd.data.model.AnnotateImageRequest
import com.example.livevoicetranslator_rd.data.model.Feature
import com.example.livevoicetranslator_rd.data.model.Image
import com.example.livevoicetranslator_rd.data.model.VisionApiRequest
import com.example.livevoicetranslator_rd.data.source.remote.api.APIDataSource
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.repository.ImageProcessingRepository

class ImageProcessingRepositoryImpl(
    private val apiDataSource: APIDataSource
) : ImageProcessingRepository {

    override suspend fun extractTextFromImage(imageBase64: String): Result<OCRResult> {
        val response = apiDataSource.callCloudVisionApi(imageBase64.toVisionApiRequest())
        return response.responses?.firstOrNull()?.textAnnotations?.firstOrNull()?.description?.let {
            Result.success(mapToOCRResult(response.responses.first()))
        } ?: Result.failure(Exception("No text found"))
    }

    override suspend fun autoRotate(image: CameraImage): Result<CameraImage> {
        TODO("Not yet implemented")
    }

    override suspend fun autoCrop(image: CameraImage): Result<CameraImage> {
        TODO("Not yet implemented")
    }

    override suspend fun adjustBrightness(
        image: CameraImage,
        factor: Float
    ): Result<CameraImage> {
        TODO("Not yet implemented")
    }

    override fun calculateImageQuality(image: CameraImage): Float {
        TODO("Not yet implemented")
    }
}