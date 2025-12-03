package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.domain.usecase.DetectLanguageUseCase
import com.example.livevoicetranslator_rd.domain.usecase.GetHistoryUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SaveFavoriteUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { TranslateTextUseCase(repo = get()) }
    single { DetectLanguageUseCase(repo = get()) }
    single { SaveFavoriteUseCase(repo = get()) }
    single { GetHistoryUseCase(repo = get()) }
}