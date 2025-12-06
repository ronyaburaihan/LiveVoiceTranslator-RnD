package com.example.livevoicetranslator_rd.data.source

actual fun provideMLKitLanguageDetection(): MLKitLanguageDetectionDataSource =
    IOSMLKitLanguageDetectionDataSource()
