package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.core.platform.ClipboardService
import com.example.livevoicetranslator_rd.core.platform.ShareService
import com.example.livevoicetranslator_rd.core.platform.TTSService
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import org.koin.android.ext.koin.androidContext
import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.data.repository.provideTranslationModelRepositoryImpl
import com.example.livevoicetranslator_rd.data.source.MLKitLanguageDetectionDataSource
import com.example.livevoicetranslator_rd.data.source.provideMLKitLanguageDetection
import com.example.livevoicetranslator_rd.data.source.provideMLKitTranslation
import com.example.livevoicetranslator_rd.domain.repository.TranslationModelRepository
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val platformModule = module {
    single<MLKitTranslationDataSource> { provideMLKitTranslation() }
    single<MLKitLanguageDetectionDataSource> { provideMLKitLanguageDetection() }
    single<TranslationModelRepository> { provideTranslationModelRepositoryImpl() }
    single { ClipboardService(androidContext()) }
    single { ShareService(androidContext()) }
    single { TTSService(androidContext()) }

    // SpeechToText singleton for Android with automatic Activity injection
    single { SpeechToText(get(), get() ) }

    // Android-specific ConversationViewModel with automatic dependency resolution
    viewModel {
        ConversationViewModel(
            speechToText = get(),
            startSpeechRecognitionUseCase = get(),
            stopSpeechRecognitionUseCase = get(),
            requestPermissionUseCase = get(),
            copyTranscriptUseCase = get(),
            initializeTTSUseCase = get(),
            observeTTSStateUseCase = get(),
            releaseTTSUseCase = get(),
            speakTextUseCase = get()
        )
    }
}