package com.example.livevoicetranslator_rd.data.source

import cocoapods.GoogleMLKit.MLKLanguageIdentification
import cocoapods.GoogleMLKit.MLKModelManager
import cocoapods.GoogleMLKit.MLKTranslateLanguage
import cocoapods.GoogleMLKit.MLKTranslateLanguageAfrikaans
import cocoapods.GoogleMLKit.MLKTranslateLanguageAlbanian
import cocoapods.GoogleMLKit.MLKTranslateLanguageArabic
import cocoapods.GoogleMLKit.MLKTranslateLanguageBelarusian
import cocoapods.GoogleMLKit.MLKTranslateLanguageBengali
import cocoapods.GoogleMLKit.MLKTranslateLanguageBulgarian
import cocoapods.GoogleMLKit.MLKTranslateLanguageCatalan
import cocoapods.GoogleMLKit.MLKTranslateLanguageChinese
import cocoapods.GoogleMLKit.MLKTranslateLanguageCroatian
import cocoapods.GoogleMLKit.MLKTranslateLanguageCzech
import cocoapods.GoogleMLKit.MLKTranslateLanguageDanish
import cocoapods.GoogleMLKit.MLKTranslateLanguageDutch
import cocoapods.GoogleMLKit.MLKTranslateLanguageEnglish
import cocoapods.GoogleMLKit.MLKTranslateLanguageEsperanto
import cocoapods.GoogleMLKit.MLKTranslateLanguageEstonian
import cocoapods.GoogleMLKit.MLKTranslateLanguageFinnish
import cocoapods.GoogleMLKit.MLKTranslateLanguageFrench
import cocoapods.GoogleMLKit.MLKTranslateLanguageGalician
import cocoapods.GoogleMLKit.MLKTranslateLanguageGeorgian
import cocoapods.GoogleMLKit.MLKTranslateLanguageGerman
import cocoapods.GoogleMLKit.MLKTranslateLanguageGreek
import cocoapods.GoogleMLKit.MLKTranslateLanguageGujarati
import cocoapods.GoogleMLKit.MLKTranslateLanguageHaitianCreole
import cocoapods.GoogleMLKit.MLKTranslateLanguageHebrew
import cocoapods.GoogleMLKit.MLKTranslateLanguageHindi
import cocoapods.GoogleMLKit.MLKTranslateLanguageHungarian
import cocoapods.GoogleMLKit.MLKTranslateLanguageIcelandic
import cocoapods.GoogleMLKit.MLKTranslateLanguageIndonesian
import cocoapods.GoogleMLKit.MLKTranslateLanguageIrish
import cocoapods.GoogleMLKit.MLKTranslateLanguageItalian
import cocoapods.GoogleMLKit.MLKTranslateLanguageJapanese
import cocoapods.GoogleMLKit.MLKTranslateLanguageKannada
import cocoapods.GoogleMLKit.MLKTranslateLanguageKorean
import cocoapods.GoogleMLKit.MLKTranslateLanguageLatvian
import cocoapods.GoogleMLKit.MLKTranslateLanguageLithuanian
import cocoapods.GoogleMLKit.MLKTranslateLanguageMacedonian
import cocoapods.GoogleMLKit.MLKTranslateLanguageMalay
import cocoapods.GoogleMLKit.MLKTranslateLanguageMaltese
import cocoapods.GoogleMLKit.MLKTranslateLanguageMarathi
import cocoapods.GoogleMLKit.MLKTranslateLanguageNorwegian
import cocoapods.GoogleMLKit.MLKTranslateLanguagePersian
import cocoapods.GoogleMLKit.MLKTranslateLanguagePolish
import cocoapods.GoogleMLKit.MLKTranslateLanguagePortuguese
import cocoapods.GoogleMLKit.MLKTranslateLanguageRomanian
import cocoapods.GoogleMLKit.MLKTranslateLanguageRussian
import cocoapods.GoogleMLKit.MLKTranslateLanguageSlovak
import cocoapods.GoogleMLKit.MLKTranslateLanguageSlovenian
import cocoapods.GoogleMLKit.MLKTranslateLanguageSpanish
import cocoapods.GoogleMLKit.MLKTranslateLanguageSwahili
import cocoapods.GoogleMLKit.MLKTranslateLanguageSwedish
import cocoapods.GoogleMLKit.MLKTranslateLanguageTagalog
import cocoapods.GoogleMLKit.MLKTranslateLanguageTamil
import cocoapods.GoogleMLKit.MLKTranslateLanguageTelugu
import cocoapods.GoogleMLKit.MLKTranslateLanguageThai
import cocoapods.GoogleMLKit.MLKTranslateLanguageTurkish
import cocoapods.GoogleMLKit.MLKTranslateLanguageUkrainian
import cocoapods.GoogleMLKit.MLKTranslateLanguageUrdu
import cocoapods.GoogleMLKit.MLKTranslateLanguageVietnamese
import cocoapods.GoogleMLKit.MLKTranslateLanguageWelsh
import cocoapods.GoogleMLKit.MLKTranslator
import cocoapods.GoogleMLKit.MLKTranslatorOptions
import kotlinx.cinterop.ExperimentalForeignApi
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
            "af" -> MLKTranslateLanguageAfrikaans
            "ar" -> MLKTranslateLanguageArabic
            "be" -> MLKTranslateLanguageBelarusian
            "bg" -> MLKTranslateLanguageBulgarian
            "bn" -> MLKTranslateLanguageBengali
            "ca" -> MLKTranslateLanguageCatalan
            "cs" -> MLKTranslateLanguageCzech
            "cy" -> MLKTranslateLanguageWelsh
            "da" -> MLKTranslateLanguageDanish
            "de" -> MLKTranslateLanguageGerman
            "el" -> MLKTranslateLanguageGreek
            "en" -> MLKTranslateLanguageEnglish
            "eo" -> MLKTranslateLanguageEsperanto
            "es" -> MLKTranslateLanguageSpanish
            "et" -> MLKTranslateLanguageEstonian
            "fa" -> MLKTranslateLanguagePersian
            "fi" -> MLKTranslateLanguageFinnish
            "fr" -> MLKTranslateLanguageFrench
            "ga" -> MLKTranslateLanguageIrish
            "gl" -> MLKTranslateLanguageGalician
            "gu" -> MLKTranslateLanguageGujarati
            "he" -> MLKTranslateLanguageHebrew
            "hi" -> MLKTranslateLanguageHindi
            "hr" -> MLKTranslateLanguageCroatian
            "ht" -> MLKTranslateLanguageHaitianCreole
            "hu" -> MLKTranslateLanguageHungarian
            "id" -> MLKTranslateLanguageIndonesian
            "is" -> MLKTranslateLanguageIcelandic
            "it" -> MLKTranslateLanguageItalian
            "ja" -> MLKTranslateLanguageJapanese
            "ka" -> MLKTranslateLanguageGeorgian
            "kn" -> MLKTranslateLanguageKannada
            "ko" -> MLKTranslateLanguageKorean
            "lt" -> MLKTranslateLanguageLithuanian
            "lv" -> MLKTranslateLanguageLatvian
            "mk" -> MLKTranslateLanguageMacedonian
            "mr" -> MLKTranslateLanguageMarathi
            "ms" -> MLKTranslateLanguageMalay
            "mt" -> MLKTranslateLanguageMaltese
            "nl" -> MLKTranslateLanguageDutch
            "no" -> MLKTranslateLanguageNorwegian
            "pl" -> MLKTranslateLanguagePolish
            "pt" -> MLKTranslateLanguagePortuguese
            "ro" -> MLKTranslateLanguageRomanian
            "ru" -> MLKTranslateLanguageRussian
            "sk" -> MLKTranslateLanguageSlovak
            "sl" -> MLKTranslateLanguageSlovenian
            "sq" -> MLKTranslateLanguageAlbanian
            "sv" -> MLKTranslateLanguageSwedish
            "sw" -> MLKTranslateLanguageSwahili
            "ta" -> MLKTranslateLanguageTamil
            "te" -> MLKTranslateLanguageTelugu
            "th" -> MLKTranslateLanguageThai
            "tl" -> MLKTranslateLanguageTagalog
            "tr" -> MLKTranslateLanguageTurkish
            "uk" -> MLKTranslateLanguageUkrainian
            "ur" -> MLKTranslateLanguageUrdu
            "vi" -> MLKTranslateLanguageVietnamese
            "zh", "zh-cn" -> MLKTranslateLanguageChinese
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
            /*MLKModelManager.modelManager().deleteDownloadedModel(
                translator.
            ) { error ->
                if (error != null) {
                    cont.resume(Result.failure(Exception(error.localizedDescription)))
                } else {
                    cont.resume(Result.success(Unit))
                }
            }*/
        } catch (e: Exception) {
            cont.resume(Result.failure(e))
        }
    }
}