package com.example.livevoicetranslator_rd.core.platform

import android.graphics.BitmapFactory
import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCREngine
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.domain.model.TextLine
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

actual class OCRProcessor actual constructor() {
    actual suspend fun recognizeText(
        image: CameraImage,
        languageHints: List<String>
    ): Result<OCRResult> {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(
                image.imageData,
                0,
                image.imageData.size
            )
            val inputImage =
                InputImage.fromBitmap(bitmap, image.rotation)
            recognizeText(inputImage, languageHints)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun downloadModel(languageCode: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    actual suspend fun isModelDownloaded(languageCode: String): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun recognizeText(
        image: InputImage,
        languageHints: List<String> = emptyList()
    ): Result<OCRResult> {
        return try {
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS
            )
            val visionText = recognizer.process(image).await()
            val blocks = visionText.textBlocks.map { block ->
                TextBlock(
                    text = block.text,
                    lines = block.lines.map { line ->
                        TextLine(
                            text = line.text,
                            confidence = line.confidence
                        )
                    },
                    boundingBox = BoundingBox(
                        left = block.boundingBox?.left?.toFloat() ?: 0f,
                        top = block.boundingBox?.top?.toFloat() ?: 0f,
                        right = block.boundingBox?.right?.toFloat() ?: 0f,
                        bottom = block.boundingBox?.bottom?.toFloat() ?: 0f,
                    )
                )
            }
            Result.success(
                OCRResult(
                    fullText = visionText.text,
                    blocks = blocks,
                    confidence = 0.0f,
                    detectedLanguage = null,
                    engine = OCREngine.ML_KIT_LATIN
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}