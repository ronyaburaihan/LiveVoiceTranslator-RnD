package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class MLKitTranslationDataSource actual constructor() : MLTranslator {

    private val identifier by lazy {
        LanguageIdentification.getClient(
            LanguageIdentificationOptions.Builder()
                .setConfidenceThreshold(0.34f)
                .build()
        )
    }

    private fun normalize(tag: String?): String? {
        if (tag.isNullOrBlank()) return null
        val cleaned = tag.lowercase(Locale.ROOT)
        val code = if (cleaned.contains("-")) cleaned.substringBefore("-") else cleaned
        return when (code) {
            "en", "es", "fr", "de", "it", "pt", "zh", "ja", "ko", "hi", "bn", "ar", "ru", "tr", "id", "vi", "th" -> code
            else -> code
        }
    }

    actual override suspend fun detectLanguage(text: String): LanguageDetectionResult =
        suspendCancellableCoroutine { cont ->
            if (text.isBlank() || text.length < 3) {
                cont.resume(LanguageDetectionResult("und", 0f))
                return@suspendCancellableCoroutine
            }

            try {
                identifier.identifyPossibleLanguages(text)
                    .addOnSuccessListener { languages ->
                        if (languages.isNotEmpty()) {
                            // Take the top language (highest confidence)
                            val topLanguage = languages.maxByOrNull { it.confidence }!!
                            cont.resume(
                                LanguageDetectionResult(
                                    topLanguage.languageTag,
                                    topLanguage.confidence
                                )
                            )
                        } else {
                            cont.resume(LanguageDetectionResult("und", 0f))
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resumeWithException(e)
                    }
            } catch (e: Exception) {
                cont.resume(LanguageDetectionResult("und", 0f))
            }
        }


    actual override suspend fun translate(text: String, sourceLang: String, targetLang: String): String =
        suspendCancellableCoroutine { cont ->
            val src = normalize(sourceLang) ?: throw Exception("Unsupported sourceLang")
            val tgt = normalize(targetLang) ?: throw Exception("Unsupported targetLang")

            val srcModel = TranslateLanguage.fromLanguageTag(src)
            val tgtModel = TranslateLanguage.fromLanguageTag(tgt)

            if (srcModel == null || tgtModel == null) {
                cont.resumeWithException(Exception("Unsupported language pair"))
                return@suspendCancellableCoroutine
            }

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(srcModel)
                .setTargetLanguage(tgtModel)
                .build()

            val translator = Translation.getClient(options)

            translator.translate(text)
                .addOnSuccessListener { translated -> cont.resume(translated) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    actual override suspend fun downloadModelIfNeeded(sourceLang: String, targetLang: String): Boolean {
        val src = normalize(sourceLang) ?: return false
        val tgt = normalize(targetLang) ?: return false

        val srcModel = TranslateLanguage.fromLanguageTag(src)
        val tgtModel = TranslateLanguage.fromLanguageTag(tgt)

        if (srcModel == null || tgtModel == null) {
            return false
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(srcModel)
            .setTargetLanguage(tgtModel)
            .build()
        val translator = Translation.getClient(options)

        return try {
            val conditions = DownloadConditions.Builder().build()
            translator.downloadModelIfNeeded(conditions).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}