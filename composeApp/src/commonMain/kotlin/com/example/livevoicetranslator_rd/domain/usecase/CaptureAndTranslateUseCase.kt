package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.CameraTranslation
import com.example.livevoicetranslator_rd.domain.model.ImageSource
import com.example.livevoicetranslator_rd.domain.repository.CameraRepository
import com.example.livevoicetranslator_rd.domain.repository.ImageProcessingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CaptureAndTranslateUseCase(
    private val cameraRepository: CameraRepository,
    private val imageProcessingRepository: ImageProcessingRepository,
    //private val translationEngine: TranslationEngine,
) {
    operator fun invoke(
        sourceLang: String,
        targetLang: String,
        source: ImageSource,
        languageHints: List<String> = emptyList()
    ): Flow<CameraTranslationState> = flow {
        try {
            emit(CameraTranslationState.Loading("Capturing image..."))

            // Step 1: Capture or pick image
            val imageResult = when (source) {
                ImageSource.CAMERA -> cameraRepository.captureImage()
                ImageSource.GALLERY -> cameraRepository.pickImageFromGallery()
            }

            val originalImage = imageResult.getOrElse { error ->
                emit(CameraTranslationState.Error("Failed to capture image: ${error.message}"))
                return@flow
            }

            emit(CameraTranslationState.ImageCaptured(originalImage))
            emit(CameraTranslationState.Loading("Processing image..."))

            // Step 2: Auto-rotate and crop
            val rotatedImage = imageProcessingRepository.autoRotate(originalImage)
                .getOrElse { originalImage }

            val croppedImage = imageProcessingRepository.autoCrop(rotatedImage)
                .getOrElse { rotatedImage }

            emit(CameraTranslationState.ImageProcessed(croppedImage))
            emit(CameraTranslationState.Loading("Recognizing text..."))

            // Step 3: OCR
            val ocrResult = cameraRepository.recognizeText(croppedImage, languageHints)
                .getOrElse { error ->
                    emit(CameraTranslationState.Error("OCR failed: ${error.message}"))
                    return@flow
                }

            if (ocrResult.fullText.isBlank()) {
                emit(CameraTranslationState.Error("No text detected in image"))
                return@flow
            }

            emit(CameraTranslationState.TextRecognized(ocrResult))
            emit(CameraTranslationState.Loading("Translating..."))

            /*// Step 4: Translate
            val translationResult = translationEngine.translate(
                text = ocrResult.fullText,
                sourceLang = ocrResult.detectedLanguage ?: sourceLang,
                targetLang = targetLang
            )

            val translation = translationResult.getOrElse { error ->
                emit(CameraTranslationState.Error("Translation failed: ${error.message}"))
                return@flow
            }*/

            // Step 5: Save to history
            val cameraTranslation = CameraTranslation(
                id = generateId(),
                imageData = croppedImage.imageData,
                detectedText = ocrResult.fullText,
                translatedText = "translation.translatedText",
                sourceLang = ocrResult.detectedLanguage ?: sourceLang,
                targetLang = targetLang,
                ocrEngine = ocrResult.engine,
            )

            cameraRepository.saveToHistory(cameraTranslation)

            emit(
                CameraTranslationState.Complete(
                    image = croppedImage,
                    ocrResult = ocrResult,
                    translation = "translation.translatedText"
                )
            )

        } catch (e: Exception) {
            emit(CameraTranslationState.Error("Unexpected error: ${e.message}"))
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun generateId(): String =
        Clock.System.now().toEpochMilliseconds().toString()
}