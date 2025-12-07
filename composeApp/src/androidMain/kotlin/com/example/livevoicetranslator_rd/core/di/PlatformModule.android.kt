package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.MLTranslator
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val platformModule = module {
    single<MLTranslator> { MLKitTranslationDataSource() }

    // SpeechToText singleton for Android with automatic Activity injection
    single { SpeechToText(get(), get()) }

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
            speakTextUseCase = get(),
            translateTextUseCase = get()
        )
    }
}