package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.repository.TTSProvider

private var ttsProvider: () -> TTSProvider? = { null }

fun setTTSProvider(provider: () -> TTSProvider) {
    ttsProvider = provider
}

actual fun getTTSProvider(): TTSProvider {
    return ttsProvider.invoke() ?: throw IllegalStateException("TTS provider not set")
}