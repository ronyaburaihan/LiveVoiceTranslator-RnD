package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.MLTranslator
import org.koin.dsl.module

actual val platformModule = module {
    single<MLTranslator> { MLKitTranslationDataSource() }
}