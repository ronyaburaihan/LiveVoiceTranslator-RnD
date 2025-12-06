package com.example.livevoicetranslator_rd.data.source

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidMLKitTranslationDataSource : MLKitTranslationDataSource {

    private fun normalize(tag: String?): String? {
        if (tag.isNullOrBlank()) return null
        val cleaned = tag.lowercase(Locale.ROOT)
        return cleaned.substringBefore("-")
    }

    override suspend fun translate(text: String, sourceLang: String, targetLang: String): String =
        suspendCancellableCoroutine { cont ->
            val src = normalize(sourceLang) ?: return@suspendCancellableCoroutine cont.resumeWithException(Exception("Unsupported source"))
            val tgt = normalize(targetLang) ?: return@suspendCancellableCoroutine cont.resumeWithException(Exception("Unsupported target"))

            val srcModel = TranslateLanguage.fromLanguageTag(src)
            val tgtModel = TranslateLanguage.fromLanguageTag(tgt)

            if (srcModel == null || tgtModel == null) {
                cont.resumeWithException(Exception("Unsupported language pair"))
                return@suspendCancellableCoroutine
            }

            val translator = Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(srcModel)
                    .setTargetLanguage(tgtModel)
                    .build()
            )

            translator.translate(text)
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    override suspend fun downloadModelIfNeeded(sourceLang: String, targetLang: String): Boolean {
        val src = normalize(sourceLang) ?: return false
        val tgt = normalize(targetLang) ?: return false

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(src) ?: return false)
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(tgt) ?: return false)
            .build()

        val translator = Translation.getClient(options)

        return try {
            translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            true
        } catch (_: Exception) {
            false
        }
    }
}