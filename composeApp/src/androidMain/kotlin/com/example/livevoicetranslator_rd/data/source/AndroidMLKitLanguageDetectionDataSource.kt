package com.example.livevoicetranslator_rd.data.source

import android.util.Log
import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidMLKitLanguageDetectionDataSource : MLKitLanguageDetectionDataSource {
    val identifier by lazy {
        LanguageIdentification.getClient(
            LanguageIdentificationOptions.Builder()
                .setConfidenceThreshold(0.34f)
                .build()
        )
    }
    override suspend fun detectLanguage(text: String): LanguageDetectionResult {

        return suspendCancellableCoroutine { cont ->
            if (text.isBlank() || text.length < 3) {
                cont.resume(LanguageDetectionResult("und", 0f))
                return@suspendCancellableCoroutine
            }

            identifier.identifyPossibleLanguages(text)
                .addOnSuccessListener { languages ->
                    val top = languages.maxByOrNull { it.confidence }
                    Log.d("AndroidMLKitLanguageDetectionDataSource", "detectLanguage: ${languages.maxByOrNull { it.confidence }}")
                    cont.resume(top?.let { LanguageDetectionResult(it.languageTag, it.confidence) }
                        ?: LanguageDetectionResult("und", 0f))
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }
    }
}