package com.example.livevoicetranslator_rd.core.platform

import android.graphics.BitmapFactory
import android.util.Log
import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCREngine
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.domain.model.TextLine
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions

actual class OCRProcessor actual constructor() {
    actual suspend fun recognizeText(
        image: CameraImage,
        language: String
    ): Result<OCRResult> {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(
                image.imageData,
                0,
                image.imageData.size
            )
            val inputImage =
                InputImage.fromBitmap(bitmap, image.rotation)
            recognizeText(inputImage, language)
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
        language: String,
    ): Result<OCRResult> {
        return try {
            val recognizer = if (language.isEmpty()) {
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            } else {
                when (language) {
                    "zh-Hans" -> TextRecognition.getClient(
                        ChineseTextRecognizerOptions.Builder().build()
                    )

                    "ja" -> TextRecognition.getClient(
                        JapaneseTextRecognizerOptions.Builder().build()
                    )

                    "ko" -> TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
                    "hi", "bn", "ne", "mr" -> TextRecognition.getClient(
                        DevanagariTextRecognizerOptions.Builder().build()
                    )

                    else -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                }
            }

            val visionText = recognizer.process(image).await()

            val rotationDegrees = image.rotationDegrees
            val imageWidth = image.width
            val imageHeight = image.height

            val blocks = visionText.textBlocks.map { block ->
                val bbox = block.boundingBox

                // Calculate rotation angle from corner points
                val rotationAngle = block.cornerPoints?.let { corners ->
                    calculateRotationAngle(corners)
                } ?: 0f

                val rotatedBox = if (bbox != null) {
                    rotateBoundingBox(bbox, rotationDegrees, imageWidth, imageHeight)
                } else {
                    BoundingBox(0f, 0f, 0f, 0f)
                }

                TextBlock(
                    text = block.text,
                    lines = block.lines.map { line ->
                        TextLine(
                            text = line.text,
                            confidence = line.confidence
                        )
                    },
                    boundingBox = rotatedBox,
                    confidence = if (block.lines.isNotEmpty()) {
                        block.lines.map { it.confidence }.average().toFloat()
                    } else {
                        0f
                    },
                    rotationAngle = rotationAngle + rotationDegrees // Combine text and image rotation
                )
            }

            val detectedLang = if (language.isEmpty() && visionText.text.isNotEmpty()) {
                detectLanguage(visionText.text)
            } else {
                language.ifEmpty { null }
            }

            val overallConfidence = if (blocks.isNotEmpty()) {
                blocks.map { it.confidence }.average().toFloat()
            } else {
                0f
            }

            val ocrResult = OCRResult(
                fullText = visionText.text,
                blocks = blocks,
                overallConfidence = overallConfidence,
                detectedLanguage = detectedLang,
                engine = OCREngine.ML_KIT_LATIN
            )
            Log.d("OCRProcessor", "recognizeText: $ocrResult")
            Log.d("OCRProcessor", "overallConfidence: ${ocrResult.overallConfidence}")
            Result.success(ocrResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateRotationAngle(corners: Array<android.graphics.Point>): Float {
        if (corners.size < 2) return 0f

        // Calculate angle from bottom-left to bottom-right corner
        val p1 = corners[0]  // Bottom-left
        val p2 = corners[1]  // Bottom-right

        val deltaX = (p2.x - p1.x).toFloat()
        val deltaY = (p2.y - p1.y).toFloat()

        // Calculate angle in degrees
        val angleRad = kotlin.math.atan2(deltaY, deltaX)
        return Math.toDegrees(angleRad.toDouble()).toFloat()
    }

    private fun rotateBoundingBox(
        bbox: android.graphics.Rect,
        rotationDegrees: Int,
        imageWidth: Int,
        imageHeight: Int
    ): BoundingBox {
        val left = bbox.left.toFloat()
        val top = bbox.top.toFloat()
        val right = bbox.right.toFloat()
        val bottom = bbox.bottom.toFloat()

        return when (rotationDegrees) {
            0 -> BoundingBox(
                left = left / imageWidth,
                top = top / imageHeight,
                right = right / imageWidth,
                bottom = bottom / imageHeight
            )

            90 -> BoundingBox(
                left = top / imageHeight,
                top = (imageWidth - right) / imageWidth,
                right = bottom / imageHeight,
                bottom = (imageWidth - left) / imageWidth
            )

            180 -> BoundingBox(
                left = (imageWidth - right) / imageWidth,
                top = (imageHeight - bottom) / imageHeight,
                right = (imageWidth - left) / imageWidth,
                bottom = (imageHeight - top) / imageHeight
            )

            270 -> BoundingBox(
                left = (imageHeight - bottom) / imageHeight,
                top = left / imageWidth,
                right = (imageHeight - top) / imageHeight,
                bottom = right / imageWidth
            )

            else -> BoundingBox(
                left = left / imageWidth,
                top = top / imageHeight,
                right = right / imageWidth,
                bottom = bottom / imageHeight
            )
        }
    }

    private suspend fun detectLanguage(text: String): String? {
        if (text.isEmpty()) return null

        return try {
            val languageIdentifier = LanguageIdentification.getClient(
                LanguageIdentificationOptions.Builder()
                    .setConfidenceThreshold(0.5f)
                    .build()
            )
            languageIdentifier.identifyLanguage(text).await()
                .takeIf { it != "und" }
        } catch (e: Exception) {
            // Fallback to script inference
            inferLanguageFromScript(text)
        }
    }

    private fun inferLanguageFromScript(text: String): String? {
        if (text.isEmpty()) return null

        return when {
            text.any { it in '\u4E00'..'\u9FFF' } -> "zh-Hans"
            text.any { it in '\u3040'..'\u309F' || it in '\u30A0'..'\u30FF' } -> "ja"
            text.any { it in '\uAC00'..'\uD7AF' } -> "ko"
            text.any { it in '\u0900'..'\u097F' } -> "hi"
            text.any { it in '\u0600'..'\u06FF' } -> "ar"
            text.any { it in '\u0E00'..'\u0E7F' } -> "th"
            text.any { it in '\u0980'..'\u09FF' } -> "bn"
            text.any { it in '\u0B80'..'\u0BFF' } -> "ta"
            text.any { it in '\u0400'..'\u04FF' } -> "ru"
            else -> "en"
        }
    }
}