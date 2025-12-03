package com.example.livevoicetranslator_rd

import android.app.Application
import com.example.livevoicetranslator_rd.core.di.initKoin
import com.example.livevoicetranslator_rd.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class VoiceTranslatorApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@VoiceTranslatorApp)
            modules(
                    module {
                single { applicationContext }
            },
                appModule
                )

        }
    }
}