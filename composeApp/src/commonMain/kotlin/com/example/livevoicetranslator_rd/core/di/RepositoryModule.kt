package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.repository.TranslationRepositoryImpl
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository
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
}