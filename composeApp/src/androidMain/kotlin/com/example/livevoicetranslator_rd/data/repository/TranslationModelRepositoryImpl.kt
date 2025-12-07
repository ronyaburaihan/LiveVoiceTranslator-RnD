package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.data.source.MlKitTranslatorProvider
import com.example.livevoicetranslator_rd.domain.model.TranslateLanguage
import com.example.livevoicetranslator_rd.domain.model.TranslationModelItem
import com.example.livevoicetranslator_rd.domain.model.TranslationModelStatus
import com.example.livevoicetranslator_rd.domain.repository.TranslationModelRepository
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TranslationModelRepositoryImpl : TranslationModelRepository {
    override suspend fun isModelDownloaded(lang: String): Boolean {
        val model = TranslateRemoteModel.Builder(lang).build()
        return RemoteModelManager.getInstance()
            .isModelDownloaded(model)
            .await()
    }

    override suspend fun detectLanguage(text: String): String {
        val client = LanguageIdentification.getClient()
        return client.identifyLanguage(text).await()
    }

    override fun observeTranslationModelStatuses(): Flow<List<TranslationModelItem>> {
        val downloaded = MlKitTranslatorProvider.downloadedModelsFlow()
        val downloading = MlKitTranslatorProvider.downloadingModelsFlow()
        val combined = combine(downloaded, downloading) { d, ing ->
            TranslateLanguage.entries.map { lang ->
                val code = langToCode(lang)
                val status = when {
                    d.contains(code) -> TranslationModelStatus.DOWNLOADED
                    ing.contains(code) -> TranslationModelStatus.DOWNLOADING
                    else -> TranslationModelStatus.NOT_DOWNLOADED
                }
                TranslationModelItem(language = lang, status = status)
            }
        }
        return flow {
            coroutineScope {
                launch {
                    try {
                        MlKitTranslatorProvider.refreshDownloadedModels()
                    } catch (_: Exception) {
                    }
                }
                emitAll(combined)
            }
        }
    }

    override suspend fun downloadModel(languageCode: String) {
        MlKitTranslatorProvider.downloadModel(languageCode)
    }

    private fun langToCode(lang: TranslateLanguage): String = when (lang) {
        TranslateLanguage.AFRIKAANS -> "af"
        TranslateLanguage.ARABIC -> "ar"
        TranslateLanguage.BELARUSSIAN -> "be"
        TranslateLanguage.BULGARIAN -> "bg"
        TranslateLanguage.BENGALI -> "bn"
        TranslateLanguage.CATALAN -> "ca"
        TranslateLanguage.CZECH -> "cs"
        TranslateLanguage.WELSH -> "cy"
        TranslateLanguage.DANISH -> "da"
        TranslateLanguage.GERMAN -> "de"
        TranslateLanguage.GREEK -> "el"
        TranslateLanguage.ENGLISH -> "en"
        TranslateLanguage.ESPERANTO -> "eo"
        TranslateLanguage.SPANISH -> "es"
        TranslateLanguage.ESTONIAN -> "et"
        TranslateLanguage.PERSIAN -> "fa"
        TranslateLanguage.FINNISH -> "fi"
        TranslateLanguage.FRENCH -> "fr"
        TranslateLanguage.IRISH -> "ga"
        TranslateLanguage.GALICIAN -> "gl"
        TranslateLanguage.GUJARATI -> "gu"
        TranslateLanguage.HEBREW -> "he"
        TranslateLanguage.HINDI -> "hi"
        TranslateLanguage.CROATIAN -> "hr"
        TranslateLanguage.HAITIAN -> "ht"
        TranslateLanguage.HUNGARIAN -> "hu"
        TranslateLanguage.INDONESIAN -> "id"
        TranslateLanguage.ICELANDIC -> "is"
        TranslateLanguage.ITALIAN -> "it"
        TranslateLanguage.JAPANESE -> "ja"
        TranslateLanguage.GEORGIAN -> "ka"
        TranslateLanguage.KANNADA -> "kn"
        TranslateLanguage.KOREAN -> "ko"
        TranslateLanguage.LITHUANIAN -> "lt"
        TranslateLanguage.LATVIAN -> "lv"
        TranslateLanguage.MACEDONIAN -> "mk"
        TranslateLanguage.MARATHI -> "mr"
        TranslateLanguage.MALAY -> "ms"
        TranslateLanguage.MALTESE -> "mt"
        TranslateLanguage.DUTCH -> "nl"
        TranslateLanguage.NORWEGIAN -> "no"
        TranslateLanguage.POLISH -> "pl"
        TranslateLanguage.PORTUGUESE -> "pt"
        TranslateLanguage.ROMANIAN -> "ro"
        TranslateLanguage.RUSSIAN -> "ru"
        TranslateLanguage.SLOVAK -> "sk"
        TranslateLanguage.SLOVENIAN -> "sl"
        TranslateLanguage.ALBANIAN -> "sq"
        TranslateLanguage.SWEDISH -> "sv"
        TranslateLanguage.SWAHILI -> "sw"
        TranslateLanguage.TAMIL -> "ta"
        TranslateLanguage.TELUGU -> "te"
        TranslateLanguage.THAI -> "th"
        TranslateLanguage.TAGALOG -> "tl"
        TranslateLanguage.TURKISH -> "tr"
        TranslateLanguage.UKRAINIAN -> "uk"
        TranslateLanguage.URDU -> "ur"
        TranslateLanguage.VIETNAMESE -> "vi"
        TranslateLanguage.CHINESE -> "zh"
    }

}
