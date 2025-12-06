package com.example.livevoicetranslator_rd.data.source

import kotlinx.cinterop.ExperimentalForeignApi
import platform.MLKitLanguageID.MLKLanguageIdentification
import platform.MLKitTranslate.MLKTranslateLanguage
import platform.MLKitTranslate.MLKTranslator
import platform.MLKitTranslate.MLKTranslatorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
actual class MLKitTranslationDataSource actual constructor() : MLTranslator {

    /**
     * Detect language using ML Kit Language ID
     */
    actual override suspend fun detectLanguage(text: String): String = suspendCoroutine { cont ->
        val languageId = MLKLanguageIdentification.languageIdentification()
        
        languageId.identifyLanguageForText(text) { languageCode, error ->
            if (error != null) {
                // Return "und" (undetermined) on error
                cont.resume("und")
            } else {
                // languageCode will be "und" if language cannot be determined
                cont.resume(languageCode ?: "und")
            }
        }
    }

    /**
     * Translate text using ML Kit Translation
     */
    actual override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String = suspendCoroutine { cont ->
        try {
            // Convert language codes to ML Kit format
            val sourceLanguage = convertToMLKitLanguage(sourceLang)
            val targetLanguage = convertToMLKitLanguage(targetLang)
            
            if (sourceLanguage == null || targetLanguage == null) {
                cont.resumeWithException(
                    Exception("Unsupported language pair: $sourceLang -> $targetLang")
                )
                return@suspendCoroutine
            }

            // Create translator options
            val options = MLKTranslatorOptions(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            
            val translator = MLKTranslator.translatorWithOptions(options)
            
            // Download model if needed and translate
            translator.downloadModelIfNeededWithCompletion { error ->
                if (error != null) {
                    cont.resumeWithException(
                        Exception("Failed to download translation model: ${error.localizedDescription}")
                    )
                } else {
                    // Model is ready, perform translation
                    translator.translateText(text) { translatedText, translationError ->
                        if (translationError != null) {
                            cont.resumeWithException(
                                Exception("Translation failed: ${translationError.localizedDescription}")
                            )
                        } else {
                            cont.resume(translatedText ?: text)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            cont.resumeWithException(e)
        }
    }

    /**
     * Convert BCP-47 language code to ML Kit language constant
     */
    private fun convertToMLKitLanguage(languageCode: String): String? {
        return when (languageCode.lowercase()) {
            "af" -> MLKTranslateLanguage.Afrikaans
            "ar" -> MLKTranslateLanguage.Arabic
            "be" -> MLKTranslateLanguage.Belarusian
            "bg" -> MLKTranslateLanguage.Bulgarian
            "bn" -> MLKTranslateLanguage.Bengali
            "ca" -> MLKTranslateLanguage.Catalan
            "cs" -> MLKTranslateLanguage.Czech
            "cy" -> MLKTranslateLanguage.Welsh
            "da" -> MLKTranslateLanguage.Danish
            "de" -> MLKTranslateLanguage.German
            "el" -> MLKTranslateLanguage.Greek
            "en" -> MLKTranslateLanguage.English
            "eo" -> MLKTranslateLanguage.Esperanto
            "es" -> MLKTranslateLanguage.Spanish
            "et" -> MLKTranslateLanguage.Estonian
            "fa" -> MLKTranslateLanguage.Persian
            "fi" -> MLKTranslateLanguage.Finnish
            "fr" -> MLKTranslateLanguage.French
            "ga" -> MLKTranslateLanguage.Irish
            "gl" -> MLKTranslateLanguage.Galician
            "gu" -> MLKTranslateLanguage.Gujarati
            "he" -> MLKTranslateLanguage.Hebrew
            "hi" -> MLKTranslateLanguage.Hindi
            "hr" -> MLKTranslateLanguage.Croatian
            "ht" -> MLKTranslateLanguage.HaitianCreole
            "hu" -> MLKTranslateLanguage.Hungarian
            "id" -> MLKTranslateLanguage.Indonesian
            "is" -> MLKTranslateLanguage.Icelandic
            "it" -> MLKTranslateLanguage.Italian
            "ja" -> MLKTranslateLanguage.Japanese
            "ka" -> MLKTranslateLanguage.Georgian
            "kn" -> MLKTranslateLanguage.Kannada
            "ko" -> MLKTranslateLanguage.Korean
            "lt" -> MLKTranslateLanguage.Lithuanian
            "lv" -> MLKTranslateLanguage.Latvian
            "mk" -> MLKTranslateLanguage.Macedonian
            "mr" -> MLKTranslateLanguage.Marathi
            "ms" -> MLKTranslateLanguage.Malay
            "mt" -> MLKTranslateLanguage.Maltese
            "nl" -> MLKTranslateLanguage.Dutch
            "no" -> MLKTranslateLanguage.Norwegian
            "pl" -> MLKTranslateLanguage.Polish
            "pt" -> MLKTranslateLanguage.Portuguese
            "ro" -> MLKTranslateLanguage.Romanian
            "ru" -> MLKTranslateLanguage.Russian
            "sk" -> MLKTranslateLanguage.Slovak
            "sl" -> MLKTranslateLanguage.Slovenian
            "sq" -> MLKTranslateLanguage.Albanian
            "sv" -> MLKTranslateLanguage.Swedish
            "sw" -> MLKTranslateLanguage.Swahili
            "ta" -> MLKTranslateLanguage.Tamil
            "te" -> MLKTranslateLanguage.Telugu
            "th" -> MLKTranslateLanguage.Thai
            "tl" -> MLKTranslateLanguage.Tagalog
            "tr" -> MLKTranslateLanguage.Turkish
            "uk" -> MLKTranslateLanguage.Ukrainian
            "ur" -> MLKTranslateLanguage.Urdu
            "vi" -> MLKTranslateLanguage.Vietnamese
            "zh", "zh-cn" -> MLKTranslateLanguage.Chinese
            "und" -> null // Undetermined language
            else -> null // Unsupported language
        }
    }

    /**
     * Check if translation model is downloaded
     */
    actual override suspend fun isModelDownloaded(
        sourceLang: String,
        targetLang: String
    ): Boolean = suspendCoroutine { cont ->
        try {
            val sourceLanguage = convertToMLKitLanguage(sourceLang)
            val targetLanguage = convertToMLKitLanguage(targetLang)
            
            if (sourceLanguage == null || targetLanguage == null) {
                cont.resume(false)
                return@suspendCoroutine
            }

            val options = MLKTranslatorOptions(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            
            val translator = MLKTranslator.translatorWithOptions(options)
            
            // Check if model is downloaded by attempting to use it
            // ML Kit will return error if model is not available
            translator.downloadModelIfNeededWithCompletion { error ->
                cont.resume(error == null)
            }
        } catch (e: Exception) {
            cont.resume(false)
        }
    }

    /**
     * Download translation model
     */
    actual override suspend fun downloadModel(
        sourceLang: String,
        targetLang: String
    ): Result<Unit> = suspendCoroutine { cont ->
        try {
            val sourceLanguage = convertToMLKitLanguage(sourceLang)
            val targetLanguage = convertToMLKitLanguage(targetLang)
            
            if (sourceLanguage == null || targetLanguage == null) {
                cont.resume(Result.failure(Exception("Unsupported language pair")))
                return@suspendCoroutine
            }

            val options = MLKTranslatorOptions(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            
            val translator = MLKTranslator.translatorWithOptions(options)
            
            translator.downloadModelIfNeededWithCompletion { error ->
                if (error != null) {
                    cont.resume(Result.failure(Exception(error.localizedDescription)))
                } else {
                    cont.resume(Result.success(Unit))
                }
            }
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }

    /**
     * Delete translation model
     */
    actual override suspend fun deleteModel(
        sourceLang: String,
        targetLang: String
    ): Result<Unit> = suspendCoroutine { cont ->
        try {
            val sourceLanguage = convertToMLKitLanguage(sourceLang)
            val targetLanguage = convertToMLKitLanguage(targetLang)
            
            if (sourceLanguage == null || targetLanguage == null) {
                cont.resume(Result.failure(Exception("Unsupported language pair")))
                return@suspendCoroutine
            }

            val options = MLKTranslatorOptions(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            
            val translator = MLKTranslator.translatorWithOptions(options)
            
            // Delete the model using model manager
            platform.MLKitCommon.MLKModelManager.modelManager().deleteDownloadedModel(
                translator.model
            ) { error ->
                if (error != null) {
                    cont.resume(Result.failure(Exception(error.localizedDescription)))
                } else {
                    cont.resume(Result.success(Unit))
                }
            }
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }
}