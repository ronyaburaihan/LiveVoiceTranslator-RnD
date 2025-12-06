package com.example.livevoicetranslator_rd.presentation.screen.phrases

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

actual class ShareManager(private val context: Context) {

    actual fun shareText(text: String, title: String?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            title?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }

        val chooser = Intent.createChooser(intent, title ?: "Share")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}


@Composable
actual fun rememberShareManager(): ShareManager {
    val context = LocalContext.current
    return remember { ShareManager(context) }
}