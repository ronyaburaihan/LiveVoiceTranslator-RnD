package com.example.livevoicetranslator_rd.core.di

import com.example.livevoicetranslator_rd.data.source.remote.api.APIDataSource
import com.example.livevoicetranslator_rd.data.source.remote.api.APIDataSourceImpl
import com.example.livevoicetranslator_rd.data.source.remote.api.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single<APIDataSource> { APIDataSourceImpl(get()) }
}