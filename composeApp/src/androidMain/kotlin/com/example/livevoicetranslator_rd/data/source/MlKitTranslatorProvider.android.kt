package com.example.livevoicetranslator_rd.data.source

import com.google.mlkit.nl.translate.*
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

actual object MlKitTranslatorProvider{
    private var cachedTranslator: Translator? = null
    private val downloadedModelCodes = MutableStateFlow<Set<String>>(emptySet())
    private val downloadingModelCodes = MutableStateFlow<Set<String>>(emptySet())

    fun getTranslator(src: String, tgt: String): Translator {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(src)
            .setTargetLanguage(tgt)
            .build()

        val newTranslator = Translation.getClient(options)
        cachedTranslator?.close()  // avoid memory leak
        cachedTranslator = newTranslator
        return newTranslator
    }

    suspend fun ensureModelDownloaded(translator: Translator) {
        translator.downloadModelIfNeeded().await()
        refreshDownloadedModels()
    }


    fun close() {
        cachedTranslator?.close()
        cachedTranslator = null
    }

    suspend fun refreshDownloadedModels() {
        val models = RemoteModelManager.getInstance()
            .getDownloadedModels(TranslateRemoteModel::class.java)
            .await()
        downloadedModelCodes.value = models.map { it.language }.toSet()
    }

    fun downloadedModelsFlow(): StateFlow<Set<String>> = downloadedModelCodes

    fun downloadingModelsFlow(): StateFlow<Set<String>> = downloadingModelCodes

    suspend fun downloadModel(languageCode: String) {
        downloadingModelCodes.value = downloadingModelCodes.value + languageCode
        try {
            val remoteModel = TranslateRemoteModel.Builder(languageCode).build()
            val conditions = com.google.mlkit.common.model.DownloadConditions.Builder().build()
            RemoteModelManager.getInstance().download(remoteModel, conditions).await()
            refreshDownloadedModels()
        } finally {
            downloadingModelCodes.value = downloadingModelCodes.value - languageCode
        }
    }
}