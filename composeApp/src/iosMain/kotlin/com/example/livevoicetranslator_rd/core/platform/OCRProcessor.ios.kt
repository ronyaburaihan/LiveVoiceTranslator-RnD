package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCREngine
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.domain.model.TextLine
import io.ktor.client.request.invoke
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectGetMaxX
import platform.CoreGraphics.CGRectGetMaxY
import platform.CoreGraphics.CGRectGetMinX
import platform.CoreGraphics.CGRectGetMinY
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRecognizeTextRequestRevision1
import platform.Vision.VNRecognizedText
import platform.Vision.VNRecognizedTextObservation
import platform.Vision.VNRequestTextRecognitionLevel
import platform.Vision.VNRequestTextRecognitionLevelAccurate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
actual class OCRProcessor actual constructor() {
    actual suspend fun recognizeText(
        image: CameraImage,
        languageHints: List<String>
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
                    val observations =
                        results as? List<VNRecognizedTextObservation> ?: emptyList()

                    val blocks = observations.mapNotNull { observation ->
                        val candidate =
                            observation.topCandidates(1.toULong()).firstOrNull()
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
                                top = CGRectGetMinY(boundingBox).toFloat(),
                                right = CGRectGetMaxX(boundingBox).toFloat(),
                                bottom = 1.0f - CGRectGetMinY(boundingBox).toFloat()
                            ),
                            confidence = candidate.confidence
                        )
                    }

                    val fullText = blocks.joinToString("\n") { it.text }

                    continuation.resume(
                        Result.success(
                            OCRResult(
                                fullText = fullText,
                                blocks = blocks,
                                confidence = blocks.map {
                                    it.lines.firstOrNull()?.confidence ?: 0f
                                }.average().toFloat(),
                                detectedLanguage = null,
                                engine = OCREngine.VISION
                            )
                        )
                    )
                }

                request.recognitionLevel = VNRequestTextRecognitionLevelAccurate
                if (languageHints.isNotEmpty()) request.recognitionLanguages = languageHints

                val handler = VNImageRequestHandler(
                    cGImage = uiImage.CGImage!!,
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
