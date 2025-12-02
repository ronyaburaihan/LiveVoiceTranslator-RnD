package com.example.livevoicetranslator_rd.core.di

import com.example.kmppractice.presentation.translate.TranslateViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        TranslateViewModel(
            translateTextUseCase = get(),
            detectLanguageUseCase = get(),
            saveFavoriteUseCase = get(),
            getHistoryUseCase = get()
        )
    }
}