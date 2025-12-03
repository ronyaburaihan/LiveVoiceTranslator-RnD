package com.example.livevoicetranslator_rd.data.source

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class MLKitTranslationDataSource actual constructor() : MLTranslator {

    // Lazy initialization with safe fallback for language identification
    private fun getLanguageIdentifier() = try {
        LanguageIdentification.getClient(
            LanguageIdentificationOptions.Builder().build()
        )
    } catch (e: IllegalStateException) {
        null // Fallback: will return "und" (undetermined)
    }

    actual override suspend fun detectLanguage(text: String): String =
        suspendCancellableCoroutine { cont ->
            val identifier = getLanguageIdentifier()
            if (identifier == null) {
                // No ML Kit delegate available, return "und" (undetermined)
                cont.resume("und")
                return@suspendCancellableCoroutine
            }
            identifier.identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    cont.resume(languageCode)
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }

    // Lazy initialization for translator with safe fallback
    private fun getTranslator(sourceLang: String, targetLang: String) = try {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()
        Translation.getClient(options)
    } catch (e: IllegalStateException) {
        null
    }

    actual override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String {
        val translator = getTranslator(sourceLang, targetLang)
            ?: // Fallback: return original text if translation client unavailable
            return text
        return suspendCancellableCoroutine { cont ->
            translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { result ->
                            cont.resume(result)
                        }
                        .addOnFailureListener { e ->
                            cont.resumeWithException(e)
                        }
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }

    actual override suspend fun downloadModelIfNeeded(source: String, target: String) {
        val src = normalize(source)
        val tgt = normalize(target)

        if (src == null || tgt == null) {
            println("this model don't Support")
            return
        }

        val srcModel = TranslateLanguage.fromLanguageTag(src)
        val tgtModel = TranslateLanguage.fromLanguageTag(tgt)

        if (srcModel == null || tgtModel == null) {
            println("this model don't Support")
            return
        }

        val client = Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(srcModel)
                .setTargetLanguage(tgtModel)
                .build()
        )

        val conditions = DownloadConditions.Builder().build()

        client.downloadModelIfNeeded(conditions)
            .addOnSuccessListener { println("Model downloaded: $source → $target") }
            .addOnFailureListener { e -> println("Model download failed: ${e.message}") }
            .await()
    }

}

private fun normalize(tag: String?): String? {
    if (tag.isNullOrBlank()) return null

    val cleaned = tag.lowercase()

    return when {
        cleaned.startsWith("en") -> "en"
        cleaned.startsWith("bn") -> "bn"
        cleaned.startsWith("hi") -> "hi"
        cleaned.startsWith("ar") -> "ar"
        cleaned.startsWith("fr") -> "fr"
        cleaned.startsWith("es") -> "es"
        else -> null
    }
}
