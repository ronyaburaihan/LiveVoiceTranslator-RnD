package com.example.livevoicetranslator_rd.data.source

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class MLKitTranslationDataSource actual constructor() : MLTranslator {

    private val languageIdentifier = LanguageIdentification.getClient(
        LanguageIdentificationOptions.Builder().build()
    )

    actual override suspend fun detectLanguage(text: String): String =
        suspendCancellableCoroutine { cont ->
            languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    cont.resume(languageCode)
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }

    actual override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()
        val translator = Translation.getClient(options)

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
}