package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.*

actual class ShareManager {

    actual fun shareText(text: String, title: String?) {
        val items = listOf(text as Any)
        presentShareSheet(items)
    }
    private fun presentShareSheet(items: List<Any>) {
        val activityViewController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication
            .keyWindow?.rootViewController

        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}

@Composable
actual fun rememberShareManager(): ShareManager {
    return remember { ShareManager() }
}