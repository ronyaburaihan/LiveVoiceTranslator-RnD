package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.core.platform.ClipboardService
import com.example.livevoicetranslator_rd.core.platform.ShareService
import com.example.livevoicetranslator_rd.core.platform.TTSService
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.MLTranslator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<MLTranslator> { MLKitTranslationDataSource() }
    single { ClipboardService(androidContext()) }
    single { ShareService(androidContext()) }
    single { TTSService(androidContext()) }
}