package com.example.livevoicetranslator_rd.presentation.screen.phrases

import platform.UIKit.UIPasteboard


//iosMain
actual class ClipboardService {
    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }

    actual fun pasteFromClipboard(): String {
        return UIPasteboard.generalPasteboard.string ?: ""
    }
}