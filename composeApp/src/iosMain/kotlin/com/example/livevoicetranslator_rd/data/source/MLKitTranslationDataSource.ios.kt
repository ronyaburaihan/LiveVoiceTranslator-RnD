package com.example.livevoicetranslator_rd.data.source

import cocoapods.MLKitLanguageID.MLKLanguageIdentification
import cocoapods.MLKitTranslate.MLKTranslateLanguageAfrikaans
import cocoapods.MLKitTranslate.MLKTranslateLanguageAlbanian
import cocoapods.MLKitTranslate.MLKTranslateLanguageArabic
import cocoapods.MLKitTranslate.MLKTranslateLanguageBelarusian
import cocoapods.MLKitTranslate.MLKTranslateLanguageBengali
import cocoapods.MLKitTranslate.MLKTranslateLanguageBulgarian
import cocoapods.MLKitTranslate.MLKTranslateLanguageCatalan
import cocoapods.MLKitTranslate.MLKTranslateLanguageChinese
import cocoapods.MLKitTranslate.MLKTranslateLanguageCroatian
import cocoapods.MLKitTranslate.MLKTranslateLanguageCzech
import cocoapods.MLKitTranslate.MLKTranslateLanguageDanish
import cocoapods.MLKitTranslate.MLKTranslateLanguageDutch
import cocoapods.MLKitTranslate.MLKTranslateLanguageEnglish
import cocoapods.MLKitTranslate.MLKTranslateLanguageEsperanto
import cocoapods.MLKitTranslate.MLKTranslateLanguageEstonian
import cocoapods.MLKitTranslate.MLKTranslateLanguageFinnish
import cocoapods.MLKitTranslate.MLKTranslateLanguageFrench
import cocoapods.MLKitTranslate.MLKTranslateLanguageGalician
import cocoapods.MLKitTranslate.MLKTranslateLanguageGeorgian
import cocoapods.MLKitTranslate.MLKTranslateLanguageGerman
import cocoapods.MLKitTranslate.MLKTranslateLanguageGreek
import cocoapods.MLKitTranslate.MLKTranslateLanguageGujarati
import cocoapods.MLKitTranslate.MLKTranslateLanguageHaitianCreole
import cocoapods.MLKitTranslate.MLKTranslateLanguageHebrew
import cocoapods.MLKitTranslate.MLKTranslateLanguageHindi
import cocoapods.MLKitTranslate.MLKTranslateLanguageHungarian
import cocoapods.MLKitTranslate.MLKTranslateLanguageIcelandic
import cocoapods.MLKitTranslate.MLKTranslateLanguageIndonesian
import cocoapods.MLKitTranslate.MLKTranslateLanguageIrish
import cocoapods.MLKitTranslate.MLKTranslateLanguageItalian
import cocoapods.MLKitTranslate.MLKTranslateLanguageJapanese
import cocoapods.MLKitTranslate.MLKTranslateLanguageKannada
import cocoapods.MLKitTranslate.MLKTranslateLanguageKorean
import cocoapods.MLKitTranslate.MLKTranslateLanguageLatvian
import cocoapods.MLKitTranslate.MLKTranslateLanguageLithuanian
import cocoapods.MLKitTranslate.MLKTranslateLanguageMacedonian
import cocoapods.MLKitTranslate.MLKTranslateLanguageMalay
import cocoapods.MLKitTranslate.MLKTranslateLanguageMaltese
import cocoapods.MLKitTranslate.MLKTranslateLanguageMarathi
import cocoapods.MLKitTranslate.MLKTranslateLanguageNorwegian
import cocoapods.MLKitTranslate.MLKTranslateLanguagePersian
import cocoapods.MLKitTranslate.MLKTranslateLanguagePolish
import cocoapods.MLKitTranslate.MLKTranslateLanguagePortuguese
import cocoapods.MLKitTranslate.MLKTranslateLanguageRomanian
import cocoapods.MLKitTranslate.MLKTranslateLanguageRussian
import cocoapods.MLKitTranslate.MLKTranslateLanguageSlovak
import cocoapods.MLKitTranslate.MLKTranslateLanguageSlovenian
import cocoapods.MLKitTranslate.MLKTranslateLanguageSpanish
import cocoapods.MLKitTranslate.MLKTranslateLanguageSwahili
import cocoapods.MLKitTranslate.MLKTranslateLanguageSwedish
import cocoapods.MLKitTranslate.MLKTranslateLanguageTagalog
import cocoapods.MLKitTranslate.MLKTranslateLanguageTamil
import cocoapods.MLKitTranslate.MLKTranslateLanguageTelugu
import cocoapods.MLKitTranslate.MLKTranslateLanguageThai
import cocoapods.MLKitTranslate.MLKTranslateLanguageTurkish
import cocoapods.MLKitTranslate.MLKTranslateLanguageUkrainian
import cocoapods.MLKitTranslate.MLKTranslateLanguageUrdu
import cocoapods.MLKitTranslate.MLKTranslateLanguageVietnamese
import cocoapods.MLKitTranslate.MLKTranslateLanguageWelsh
import cocoapods.MLKitTranslate.MLKTranslator
import cocoapods.MLKitTranslate.MLKTranslatorOptions
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