package com.example.livevoicetranslator_rd.core.platform

actual class ClipboardService {
    actual fun copyToClipboard(text: String) {
    }

    actual fun pasteFromClipboard(): String {
        TODO("Not yet implemented")
    }
}

actual class ShareService {
    actual fun share(text: String, subject: String?) {
    }
}

actual class TTSService {
    actual suspend fun speak(
        text: String,
        lang: String?,
        rate: Float,
        gender: String?
    ) {
        TODO("Not yet implemented")
    }
}

actual class AppUtilsService {
    actual fun shareApp() {
        // TODO:
    }

    actual fun rateApp() {
        // TODO:
    }

    actual fun contactUs(userType: String) {
        // TODO:
    }
}