package com.example.livevoicetranslator_rd.core.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        platformModule,
        localModule
    )
}