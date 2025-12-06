package com.example.livevoicetranslator_rd.core.platform

expect class ClipboardService {
    fun copyToClipboard(text: String)
    fun pasteFromClipboard(): String
}

expect class ShareService {
    fun share(text: String, subject: String? = null)
}

expect class TTSService {
    suspend fun speak(text: String, lang: String? = null, rate: Float = 1.0f, gender: String? = null)
}

expect class AppUtilsService {
    fun shareApp()
    fun rateApp()
    fun contactUs(userType: String)
}