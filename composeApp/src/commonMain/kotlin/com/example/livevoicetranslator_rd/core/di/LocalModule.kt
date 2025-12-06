package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.source.AITranslationDataSource
import com.example.livevoicetranslator_rd.data.source.AITranslationDataSourceImpl
import com.example.livevoicetranslator_rd.data.source.CloudTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.CloudTranslationDataSourceImpl
import com.example.livevoicetranslator_rd.data.source.HistoryDataSource
import com.example.livevoicetranslator_rd.data.source.LocalCacheDataSource
import com.example.livevoicetranslator_rd.data.source.MLKitLanguageDetectionDataSource
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.TranslationOrchestratorDataSource
import com.example.livevoicetranslator_rd.data.source.provideMLKitLanguageDetection
import com.example.livevoicetranslator_rd.data.source.provideMLKitTranslation
import com.example.livevoicetranslator_rd.data.source.provideOrchestrator
import org.koin.dsl.module

val localModule = module {
    single<CloudTranslationDataSource> { CloudTranslationDataSourceImpl() }
    single<AITranslationDataSource> { AITranslationDataSourceImpl() }
    single { LocalCacheDataSource() }
    single { HistoryDataSource() }
    single<MLKitTranslationDataSource> { provideMLKitTranslation() }
    single<MLKitLanguageDetectionDataSource> { provideMLKitLanguageDetection() }
    single<TranslationOrchestratorDataSource> { provideOrchestrator() }
}