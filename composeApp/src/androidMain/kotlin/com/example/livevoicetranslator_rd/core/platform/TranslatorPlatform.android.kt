package com.example.livevoicetranslator_rd.core.platform

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.text.lowercase

actual object TranslatorPlatform {
    actual suspend fun translate(text: String, sourceLang: String, targetLang: String): String {
        // Map language codes to ML Kit language constants
        val sourceLanguage = getTranslateLanguage(sourceLang)
        val targetLanguage = getTranslateLanguage(targetLang)

        // Validate languages are supported
        if (sourceLanguage == null) {
            throw kotlin.IllegalArgumentException("Unsupported source language: $sourceLang")
        }
        if (targetLanguage == null) {
            throw kotlin.IllegalArgumentException("Unsupported target language: $targetLang")
        }

        // Create translator options
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val client = Translation.getClient(options)

        try {
            // Download model if needed with Wi-Fi requirement
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            // Download the translation model
            client.downloadModelIfNeeded(conditions).await()

            // Perform the translation
            val translated = client.translate(text).await()
            return translated
        } finally {
            // Always close the client to free resources
            client.close()
        }
    }

    /**
     * Map language codes to ML Kit TranslateLanguage constants
     * Supports common language codes like "en", "es", "fr", "de", "it", "pt", "ru", "ja", "ko", "zh", "ar", "hi", "bn"
     */
    private fun getTranslateLanguage(languageCode: String): String? {
        return when (languageCode.lowercase()) {
            "en" -> TranslateLanguage.ENGLISH
            "es" -> TranslateLanguage.SPANISH
            "fr" -> TranslateLanguage.FRENCH
            "de" -> TranslateLanguage.GERMAN
            "it" -> TranslateLanguage.ITALIAN
            "pt" -> TranslateLanguage.PORTUGUESE
            "ru" -> TranslateLanguage.RUSSIAN
            "ja" -> TranslateLanguage.JAPANESE
            "ko" -> TranslateLanguage.KOREAN
            "zh" -> TranslateLanguage.CHINESE
            "ar" -> TranslateLanguage.ARABIC
            "hi" -> TranslateLanguage.HINDI
            "bn" -> TranslateLanguage.BENGALI
            "tr" -> TranslateLanguage.TURKISH
            "pl" -> TranslateLanguage.POLISH
            "nl" -> TranslateLanguage.DUTCH
            "sv" -> TranslateLanguage.SWEDISH
            "da" -> TranslateLanguage.DANISH
            "no" -> TranslateLanguage.NORWEGIAN
            "fi" -> TranslateLanguage.FINNISH
            "cs" -> TranslateLanguage.CZECH
            "sk" -> TranslateLanguage.SLOVAK
            "hu" -> TranslateLanguage.HUNGARIAN
            "ro" -> TranslateLanguage.ROMANIAN
            "bg" -> TranslateLanguage.BULGARIAN
            "hr" -> TranslateLanguage.CROATIAN
            "sl" -> TranslateLanguage.SLOVENIAN
            "et" -> TranslateLanguage.ESTONIAN
            "lv" -> TranslateLanguage.LATVIAN
            "lt" -> TranslateLanguage.LITHUANIAN
            "uk" -> TranslateLanguage.UKRAINIAN
            "el" -> TranslateLanguage.GREEK
            "he" -> TranslateLanguage.HEBREW
            "th" -> TranslateLanguage.THAI
            "vi" -> TranslateLanguage.VIETNAMESE
            "ms" -> TranslateLanguage.MALAY
            "id" -> TranslateLanguage.INDONESIAN
            "tl" -> TranslateLanguage.TAGALOG
            "gu" -> TranslateLanguage.GUJARATI
            "mr" -> TranslateLanguage.MARATHI
            "ta" -> TranslateLanguage.TAMIL
            "te" -> TranslateLanguage.TELUGU
            "kn" -> TranslateLanguage.KANNADA
            "ur" -> TranslateLanguage.URDU
            "fa" -> TranslateLanguage.PERSIAN
            "sw" -> TranslateLanguage.SWAHILI
            "af" -> TranslateLanguage.AFRIKAANS
            "be" -> TranslateLanguage.BELARUSIAN
            "ka" -> TranslateLanguage.GEORGIAN
            "is" -> TranslateLanguage.ICELANDIC
            "ga" -> TranslateLanguage.IRISH
            "mt" -> TranslateLanguage.MALTESE
            "ca" -> TranslateLanguage.CATALAN
            "gl" -> TranslateLanguage.GALICIAN
            "cy" -> TranslateLanguage.WELSH
            "is" -> TranslateLanguage.ICELANDIC
            else -> null
        }
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { result ->
        if (cont.isActive) cont.resume(result)
    }
    addOnFailureListener { e ->
        if (cont.isActive) cont.resumeWithException(e)
    }
    addOnCanceledListener {
        if (cont.isActive) cont.cancel()
    }
}

