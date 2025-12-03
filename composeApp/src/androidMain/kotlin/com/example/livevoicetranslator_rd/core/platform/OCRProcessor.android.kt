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
                        left = (block.boundingBox?.left?.toFloat() ?: 0f) / image.width.toFloat(),
                        top = (block.boundingBox?.top?.toFloat() ?: 0f) / image.height.toFloat(),
                        right = (block.boundingBox?.right?.toFloat() ?: 0f) / image.width.toFloat(),
                        bottom = (block.boundingBox?.bottom?.toFloat()
                            ?: 0f) / image.height.toFloat(),
                    ),
                    confidence = if (block.lines.isNotEmpty()) {
                        block.lines.map { it.confidence }.average().toFloat()
                    } else {
                        0f
                    }
                )
            }

            // Detect language using ML Kit Language Identification
            val detectedLang = if (language.isEmpty() && visionText.text.isNotEmpty()) {
                val languageIdentifier = LanguageIdentification.getClient(
                    LanguageIdentificationOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build()
                )
                try {
                    languageIdentifier.identifyLanguage(visionText.text).await()
                        .takeIf { it != "und" } // "und" means undetermined
                } catch (e: Exception) {
                    null
                }
            } else {
                language.ifEmpty { null }
            }

            val overallConfidence = if (blocks.isNotEmpty()) {
                blocks.map { it.confidence }.average().toFloat()
            } else {
                0f
            }

            Result.success(
                OCRResult(
                    fullText = visionText.text,
                    blocks = blocks,
                    confidence = overallConfidence,
                    detectedLanguage = detectedLang,
                    engine = OCREngine.ML_KIT_LATIN
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}