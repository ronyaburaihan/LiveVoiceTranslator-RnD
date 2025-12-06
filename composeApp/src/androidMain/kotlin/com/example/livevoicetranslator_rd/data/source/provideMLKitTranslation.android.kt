package com.example.livevoicetranslator_rd.data.source

actual fun provideMLKitTranslation(): MLKitTranslationDataSource =
    AndroidMLKitTranslationDataSource()
