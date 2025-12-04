package com.example.livevoicetranslator_rd.core.platform

import cocoapods.MLKitTranslate.*
import cocoapods.MLKitLanguageID.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.collections.getOrPut
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.text.isBlank
import kotlin.text.lowercase

@OptIn(ExperimentalForeignApi::class)
actual object TranslatorPlatform {

    private val translators = mutableMapOf<String, MLKTranslator>()
    private val mutex = Mutex()

    actual suspend fun translate(text: String, sourceLang: String, targetLang: String): String {
        if (text.isBlank()) return ""

        return try {
            val sourceLanguage = getMLKitLanguage(sourceLang)
                ?: throw IllegalArgumentException("Unsupported source language: $sourceLang")
            val targetLanguage = getMLKitLanguage(targetLang)
                ?: throw IllegalArgumentException("Unsupported target language: $targetLang")

            val translator = getTranslator(sourceLanguage, targetLanguage)

            suspendCancellableCoroutine { continuation ->
                try {
                    val conditions = MLKModelDownloadConditions(
                        allowsCellularAccess = true,
                        allowsBackgroundDownloading = true
                    )

                    translator.downloadModelIfNeededWithConditions(conditions) { error ->
                        try {
                            if (error != null) {
                                continuation.resumeWithException(
                                    Exception("Failed to download model: ${error.localizedDescription}")
                                )
                                return@downloadModelIfNeededWithConditions
                            }

                            translator.translateText(text) { result, translateError ->
                                try {
                                    if (translateError != null) {
                                        continuation.resumeWithException(
                                            Exception("Translation failed: ${translateError.localizedDescription}")
                                        )
                                    } else {
                                        continuation.resume(result ?: "")
                                    }
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                }
                            }
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        } catch (e: Exception) {
            println("TranslatorPlatform.iOS: Translation error - ${e.message}")
            // Return fallback text instead of crashing
            "[$targetLang] $text"
        }
    }

    private suspend fun getTranslator(source: String, target: String): MLKTranslator {
        return mutex.withLock {
            val key = "${source}_$target"
            translators.getOrPut(key) {
                try {
                    val options = MLKTranslatorOptions(sourceLanguage = source, targetLanguage = target)
                    val translator = MLKTranslator.translatorWithOptions(options)
                    if (translator == null) {
                        throw IllegalStateException("Failed to create MLKTranslator with options")
                    }
                    translator
                } catch (e: Exception) {
                    println("TranslatorPlatform.iOS: Failed to create translator - ${e.message}")
                    throw e
                }
            }
        }
    }

    // Helper to detect language
    suspend fun detectLanguage(text: String): String {
        if (text.isBlank()) throw IllegalArgumentException("Text cannot be empty")

        return suspendCancellableCoroutine { continuation ->
            val languageId = MLKLanguageIdentification.languageIdentification()
            languageId.identifyLanguageForText(text) { languageCode, error ->
                if (error != null) {
                    continuation.resumeWithException(Exception("Language detection failed: ${error.localizedDescription}"))
                } else {
                    continuation.resume(languageCode ?: "und")
                }
            }
        }
    }

    // Helper to check model status
    suspend fun isModelDownloaded(sourceLang: String): Boolean {
        val sourceLanguage = getMLKitLanguage(sourceLang) ?: return false

        return suspendCancellableCoroutine { continuation ->
            val modelManager = MLKModelManager.modelManager()
            val model = MLKTranslateRemoteModel.translateRemoteModelWithLanguage(sourceLanguage)
            continuation.resume(modelManager.isModelDownloaded(model))
        }
    }

    // Helper to delete model
    suspend fun deleteModel(language: String): Boolean {
        val mlkitLanguage = getMLKitLanguage(language) ?: return false
        return suspendCancellableCoroutine { continuation ->
            val modelManager = MLKModelManager.modelManager()
            val model = MLKTranslateRemoteModel.translateRemoteModelWithLanguage(mlkitLanguage)
            modelManager.deleteDownloadedModel(model) { error ->
                continuation.resume(error == null)
            }
        }
    }

    private fun getMLKitLanguage(languageCode: String): String? {
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
            "zh" -> MLKTranslateLanguageChinese
            else -> null
        }
    }
}
