package com.example.livevoicetranslator_rd.core.platform


import com.example.livevoicetranslator_rd.data.source.AndroidTTSProvider
import com.example.livevoicetranslator_rd.domain.repository.TTSProvider

actual fun getTTSProvider(): TTSProvider {
    return AndroidTTSProvider()
}