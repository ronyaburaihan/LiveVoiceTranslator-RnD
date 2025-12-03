package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.presentation.screen.translate.TranslateViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        TranslateViewModel(
            clipboardService = get(),
            shareService = get(),
            ttsService = get(),
            translateTextUseCase = get(),
            detectLanguageUseCase = get(),
            saveFavoriteUseCase = get(),
            getHistoryUseCase = get()
        )
    }
}