package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.source.CloudTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.HistoryDataSource
import com.example.livevoicetranslator_rd.data.source.LocalCacheDataSource
import org.koin.dsl.module

val localModule = module {
    single { CloudTranslationDataSource() }
    single { LocalCacheDataSource() }
    single { HistoryDataSource() }
}