package com.example.livevoicetranslator_rd.presentation.screen.phrases

import android.content.ClipData
import android.content.ClipboardManager as AndroidClipboardManager
import android.content.Context

//androidMain
actual class ClipboardService(private val context: Context) {
    actual fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as AndroidClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }

    actual fun pasteFromClipboard(): String {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as AndroidClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).coerceToText(context).toString()
        } else {
            ""
        }
    }
}