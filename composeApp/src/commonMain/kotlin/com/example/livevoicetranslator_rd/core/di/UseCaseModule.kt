package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.domain.usecase.DetectLanguageUseCase
import com.example.livevoicetranslator_rd.domain.usecase.GetHistoryUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SaveFavoriteUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.CopyTranscriptUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.RequestPermissionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StartSpeechRecognitionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StopSpeechRecognitionUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { TranslateTextUseCase(repo = get()) }
    single { DetectLanguageUseCase(repo = get()) }
    single { SaveFavoriteUseCase(repo = get()) }
    single { GetHistoryUseCase(repo = get()) }
    
    // Speech recognition use cases
    single { StartSpeechRecognitionUseCase(speechToTextRepository = get()) }
    single { StopSpeechRecognitionUseCase(speechToTextRepository = get()) }
    single { RequestPermissionUseCase(speechToTextRepository = get()) }
    single { CopyTranscriptUseCase(speechToTextRepository = get()) }
}