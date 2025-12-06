package com.example.livevoicetranslator_rd.data.source

actual fun provideOrchestrator(): TranslationOrchestratorDataSource =
    IOSTranslationOrchestratorDataSource()
