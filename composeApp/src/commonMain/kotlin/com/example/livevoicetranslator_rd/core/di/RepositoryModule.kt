package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.repository.ImageProcessingRepositoryImpl
import com.example.livevoicetranslator_rd.data.repository.TranslationRepositoryImpl
import com.example.livevoicetranslator_rd.data.repository.SpeechToTextRepositoryImpl
import com.example.livevoicetranslator_rd.data.repository.TextToSpeechRepositoryImpl
import com.example.livevoicetranslator_rd.domain.repository.ImageProcessingRepository
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TranslationRepository> {
        TranslationRepositoryImpl(
            mlKit = get(),
            cloud = get(),
            cache = get(),
            history = get()
        )
    }

    single<SpeechToTextRepository> { SpeechToTextRepositoryImpl(get()) }
    
    single<TextToSpeechRepository> { TextToSpeechRepositoryImpl() }

    single<ImageProcessingRepository>{ ImageProcessingRepositoryImpl(get()) }
}
