package com.example.livevoicetranslator_rd.data.source

import android.util.Log
import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationEngine
import com.example.livevoicetranslator_rd.domain.util.LanguageTiers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.Exception
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AndroidTranslationOrchestratorDataSource(
    private val ai: AITranslationDataSourceImpl,
    private val mlKit: MLKitTranslationDataSource,
    private val cloud: CloudTranslationDataSourceImpl,
    private val langDetector: MLKitLanguageDetectionDataSource
): TranslationOrchestratorDataSource {
    // Configurable timeouts
    var aiTimeout: Duration = 2.seconds    // try AI for 2s before falling back
    var mlkitDownloadTimeout: Duration = 40.seconds // wait up to 10s for model download
    var cloudTimeout: Duration = 8.seconds

    // Simple mutex to avoid concurrent downloads for same pair
    private val downloadMutex = Mutex()

    override suspend fun detectLanguage(text: String): LanguageDetectionResult {
        Log.d("AndroidTranslationOrchestratorDataSource", "detectLanguage: here i am")
        val result = langDetector.detectLanguage(text)
        return if (result.confidence < 0.5f) {
            // optional: fallback to Cloud detection
            // dummy result
            LanguageDetectionResult(
                languageCode = "en",
                confidence = 1f
            )
        } else result
    }

    override suspend fun translate(
        text: String,
        source: String?, // may be null/"und"
        target: String
    ): OrchestratorResult {
        Log.d("AndroidTranslationOrchestratorDataSource", "translate: here i am")
        // Normalize codes (lowercase two-letter) - implement/replace with your util
        val srcNorm = normalizeLangOrNull(source)
        val tgtNorm = normalizeLangOrNull(target) ?: target

        // decide priority
//        val useAiPreferred = shouldPreferAi(srcNorm, tgtNorm)
//
//        // Try AI first if preferred
//        if (useAiPreferred) {
//            val aiResult = tryAi(text, srcNorm, tgtNorm)
//            if (aiResult.success) return aiResult
//            // else fall through to MLKit or Cloud
//        }

        // Try ML Kit on-device (download model if needed)
        val mlResult = tryMlKit(text, srcLang = srcNorm, targetLang = tgtNorm)
        if (mlResult.success) return mlResult

        // If AI wasn't preferred earlier, try AI now (maybe MLKit unsupported)
//        if (!useAiPreferred) {
//            val aiResult = tryAi(text, srcNorm, tgtNorm)
//            if (aiResult.success) return aiResult
//        }

        // As last resort, try Cloud
        val cloudResult = tryCloud(text, srcNorm, tgtNorm)
//        return cloudResult // could be success or failure
        return mlResult // could be success or failure
    }

    private suspend fun tryAi(text: String, sourceLang: String?, targetLang: String): OrchestratorResult {
        return withTimeoutOrNull(aiTimeout.inWholeMilliseconds) {
            try {
                val out = ai.translateAi(text, sourceLang, targetLang)
                Log.d("AndroidTranslationOrchestratorDataSource", "tryAi: success")
                OrchestratorResult(
                    success = true,
                    engine = TranslationEngine.AI,
                    translatedText = out
                )
            } catch (e: Exception) {
                Log.d("AndroidTranslationOrchestratorDataSource", "tryAi: fail")
                OrchestratorResult(success = false, engine = TranslationEngine.AI, error = e)
            }
        } ?: OrchestratorResult(
            success = false,
            engine = TranslationEngine.AI,
            error = Exception("AI timeout")
        )
    }

    private suspend fun tryMlKit(text: String, srcLang: String?, targetLang: String): OrchestratorResult {
        Log.d("AndroidTranslationOrchestratorDataSource", "tryMlKit: here i am")
        // If MLKit says not supported quickly, return failure
        // We'll attempt to download if it seems supported by MLKit (normalize check inside)
        val downloadSucceeded = downloadMutex.withLock {
            try {
                withTimeoutOrNull(mlkitDownloadTimeout.inWholeMilliseconds) {
                    mlKit.downloadModelIfNeeded(srcLang ?: "und", targetLang)
                } ?: false
            } catch (e: Exception) {
                false
            }
        }

        if (!downloadSucceeded) {
            return OrchestratorResult(
                success = false,
                engine = TranslationEngine.MLKIT_ON_DEVICE,
                error = Exception("ML Kit model not available")
            )
        }

        // now translate
        return try {
            val out = mlKit.translate(text, srcLang ?: "und", targetLang)
            OrchestratorResult(
                success = true,
                engine = TranslationEngine.MLKIT_ON_DEVICE,
                translatedText = out
            )
        } catch (e: Exception) {
            OrchestratorResult(
                success = false,
                engine = TranslationEngine.MLKIT_ON_DEVICE,
                error = e
            )
        }
    }

    private suspend fun tryCloud(text: String, sourceLang: String?, targetLang: String): OrchestratorResult {
        Log.d("AndroidTranslationOrchestratorDataSource", "tryCloud: here i am")
        return withTimeoutOrNull(cloudTimeout.inWholeMilliseconds) {
            try {
                val out = cloud.translateOnline(text, sourceLang, targetLang)
                OrchestratorResult(
                    success = true,
                    engine = TranslationEngine.CLOUD,
                    translatedText = out
                )
            } catch (e: Exception) {
                OrchestratorResult(success = false, engine = TranslationEngine.CLOUD, error = e)
            }
        } ?: OrchestratorResult(
            success = false,
            engine = TranslationEngine.CLOUD,
            error = Exception("Cloud timeout")
        )
    }

    private fun shouldPreferAi(source: String?, target: String): Boolean {
        // prefer AI for Tier A or Tier B languages (either source or target in those tiers)
        val src = source?.lowercase()
        val tgt = target.lowercase()
        return LanguageTiers.isTierA(src) || LanguageTiers.isTierB(src)
                || LanguageTiers.isTierA(tgt) || LanguageTiers.isTierB(tgt)
    }

    private fun normalizeLangOrNull(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        val cleaned = raw.lowercase().substringBefore("-")
        return if (cleaned == "und" || cleaned == "auto") null else cleaned
    }
}