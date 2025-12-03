package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCREngine
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.domain.model.TextLine
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectGetMaxX
import platform.CoreGraphics.CGRectGetMaxY
import platform.CoreGraphics.CGRectGetMinX
import platform.CoreGraphics.CGRectGetMinY
import platform.Foundation.NSData
import platform.Foundation.create
import platform.NaturalLanguage.NLLanguageArabic
import platform.NaturalLanguage.NLLanguageBengali
import platform.NaturalLanguage.NLLanguageEnglish
import platform.NaturalLanguage.NLLanguageHindi
import platform.NaturalLanguage.NLLanguageIndonesian
import platform.NaturalLanguage.NLLanguageJapanese
import platform.NaturalLanguage.NLLanguageKorean
import platform.NaturalLanguage.NLLanguagePersian
import platform.NaturalLanguage.NLLanguagePolish
import platform.NaturalLanguage.NLLanguagePortuguese
import platform.NaturalLanguage.NLLanguageRecognizer
import platform.NaturalLanguage.NLLanguageRussian
import platform.NaturalLanguage.NLLanguageSimplifiedChinese
import platform.NaturalLanguage.NLLanguageSpanish
import platform.NaturalLanguage.NLLanguageTamil
import platform.NaturalLanguage.NLLanguageThai
import platform.NaturalLanguage.NLLanguageTraditionalChinese
import platform.NaturalLanguage.NLLanguageTurkish
import platform.NaturalLanguage.NLLanguageVietnamese
import platform.UIKit.UIImage
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRecognizedText
import platform.Vision.VNRecognizedTextObservation
import platform.Vision.VNRequestTextRecognitionLevelAccurate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
actual class OCRProcessor actual constructor() {
    @OptIn(BetaInteropApi::class)
    actual suspend fun recognizeText(
        image: CameraImage,
        language: String
    ): Result<OCRResult> {
        return try {
            val imageData = image.imageData

            val nsData = imageData.usePinned { pinned ->
                NSData.create(
                    bytes = pinned.addressOf(0),
                    length = imageData.size.toULong()
                )
            }

            val uiImage = UIImage.imageWithData(nsData)
                ?: return Result.failure(Exception("Failed to create UIImage"))

            suspendCoroutine { continuation ->
                val request = VNRecognizeTextRequest { request, error ->
                    if (error != null) {
                        continuation.resume(Result.failure(Exception(error.localizedDescription)))
                        return@VNRecognizeTextRequest
                    }

                    val results = request?.results
                    val observations = results as? List<VNRecognizedTextObservation> ?: emptyList()

                    val blocks = observations.mapNotNull { observation ->
                        val candidate = observation.topCandidates(1.toULong()).firstOrNull()
                                as? VNRecognizedText ?: return@mapNotNull null

                        val boundingBox = observation.boundingBox

                        TextBlock(
                            text = candidate.string,
                            lines = listOf(
                                TextLine(
                                    text = candidate.string,
                                    confidence = candidate.confidence
                                )
                            ),
                            boundingBox = BoundingBox(
                                left = CGRectGetMinX(boundingBox).toFloat(),
                                top = 1.0f - CGRectGetMaxY(boundingBox).toFloat(),
                                right = CGRectGetMaxX(boundingBox).toFloat(),
                                bottom = 1.0f - CGRectGetMinY(boundingBox).toFloat()
                            ),
                            confidence = candidate.confidence
                        )
                    }

                    val fullText = blocks.joinToString("\n") { it.text }

                    val detectedLang = if (fullText.isNotEmpty()) {
                        val recognizer = NLLanguageRecognizer()
                        recognizer.processString(fullText)

                        // Get the dominant language as a string
                        recognizer.dominantLanguage
                    } else null

                    continuation.resume(
                        Result.success(
                            OCRResult(
                                fullText = fullText,
                                blocks = blocks,
                                confidence = if (blocks.isNotEmpty()) {
                                    blocks.map {
                                        it.lines.firstOrNull()?.confidence ?: 0f
                                    }.average().toFloat()
                                } else 0f,
                                detectedLanguage = detectedLang,
                                engine = OCREngine.VISION
                            )
                        )
                    )
                }

                request.recognitionLevel = VNRequestTextRecognitionLevelAccurate

                /*println("language: $language")

                // Set recognition languages with proper locale codes
                request.recognitionLanguages = when {
                    language.isEmpty() -> listOf("en-US")
                    language == "hi" -> listOf("hi-IN")
                    language == "bn" -> listOf("bn-IN")
                    language == "zh-Hans" -> listOf("zh-Hans")
                    language == "zh-Hant" -> listOf("zh-Hant")
                    language == "ar" -> listOf("ar")
                    language == "ko" -> listOf("ko")
                    language == "ja" -> listOf("ja")
                    else -> listOf(language)
                }

                println("recognitionLanguages: ${request.recognitionLanguages}")*/

                request.usesLanguageCorrection = true
                request.automaticallyDetectsLanguage = true  // Disable to prevent conflicts

                val handler = VNImageRequestHandler(
                    cGImage = uiImage.CGImage!!,
                    orientation = uiImage.imageOrientation.value.toUInt(),
                    options = emptyMap<Any?, Any?>()
                )

                handler.performRequests(listOf(request), null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun downloadModel(languageCode: String): Result<Unit> =
        Result.success(Unit)

    actual suspend fun isModelDownloaded(languageCode: String): Boolean = true
}
