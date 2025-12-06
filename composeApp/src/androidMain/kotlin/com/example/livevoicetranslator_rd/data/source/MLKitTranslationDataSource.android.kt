package com.example.livevoicetranslator_rd.data.source

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
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

    actual override suspend fun isModelDownloaded(
        sourceLang: String,
        targetLang: String
    ): Boolean = suspendCancellableCoroutine { cont ->
        val translator = getTranslator(sourceLang, targetLang)
        if (translator == null) {
            cont.resume(false)
            return@suspendCancellableCoroutine
        }
        
        // Check if model is downloaded
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                // Model was already downloaded or just downloaded
                cont.resume(true)
            }
            .addOnFailureListener {
                cont.resume(false)
            }
    }

    actual override suspend fun downloadModel(
        sourceLang: String,
        targetLang: String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->
        val translator = getTranslator(sourceLang, targetLang)
        if (translator == null) {
            cont.resume(Result.failure(Exception("Failed to create translator")))
            return@suspendCancellableCoroutine
        }
        
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                cont.resume(Result.success(Unit))
            }
            .addOnFailureListener { e ->
                cont.resume(Result.failure(e))
            }
    }

    actual override suspend fun deleteModel(
        sourceLang: String,
        targetLang: String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->
        val translator = getTranslator(sourceLang, targetLang)
        if (translator == null) {
            cont.resume(Result.failure(Exception("Failed to create translator")))
            return@suspendCancellableCoroutine
        }
        
        translator.close()
        // Note: ML Kit doesn't provide a direct API to delete models
        // Models are managed by Google Play Services
        cont.resume(Result.success(Unit))
    }
}