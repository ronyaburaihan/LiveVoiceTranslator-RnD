package com.example.livevoicetranslator_rd.data.source

actual fun provideOrchestrator(): TranslationOrchestratorDataSource =
    AndroidTranslationOrchestratorDataSource(
        ai = AITranslationDataSourceImpl(),
        mlKit = AndroidMLKitTranslationDataSource(),
        cloud = CloudTranslationDataSourceImpl(),
        langDetector = AndroidMLKitLanguageDetectionDataSource()
    )