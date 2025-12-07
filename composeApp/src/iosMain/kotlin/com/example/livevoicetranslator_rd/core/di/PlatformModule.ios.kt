package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.MLTranslator
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationViewModel
import org.koin.dsl.module

actual val platformModule = module {
    single<MLTranslator> { MLKitTranslationDataSource() }

    // SpeechToText singleton for iOS (no parameters needed)
    single { SpeechToText() }

    // ConversationViewModel factory for iOS
    // ConversationViewModel factory for iOS
    factory {
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