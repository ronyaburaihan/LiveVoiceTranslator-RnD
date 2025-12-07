package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.presentation.screen.camera.result.ResultViewModel
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationViewModel
import com.example.livevoicetranslator_rd.presentation.screen.translate.TranslateViewModel
import livevoicetranslatorrd.composeapp.generated.resources.Res
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
    viewModel {
        ResultViewModel(
            get(),
            get(),
            get()
        )
    }
    
    // ConversationViewModel is registered in platform-specific modules
    // to allow proper injection of platform-specific SpeechToText
}