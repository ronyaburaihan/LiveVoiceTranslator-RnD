package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.core.platform.ClipboardService
import com.example.livevoicetranslator_rd.core.platform.ShareService
import com.example.livevoicetranslator_rd.core.platform.TTSService
import com.example.livevoicetranslator_rd.data.source.MLKitTranslationDataSource
import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.data.source.MLKitLanguageDetectionDataSource
import com.example.livevoicetranslator_rd.data.source.provideMLKitLanguageDetection
import com.example.livevoicetranslator_rd.data.source.provideMLKitTranslation
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationViewModel
import org.koin.dsl.module

actual val platformModule = module {
    single<MLKitTranslationDataSource> { provideMLKitTranslation() }
    single<MLKitLanguageDetectionDataSource> { provideMLKitLanguageDetection() }
    single { ClipboardService() }
    single { ShareService() }
    single { TTSService() }

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
            copyTranscriptUseCase = get()
        )
    }
}