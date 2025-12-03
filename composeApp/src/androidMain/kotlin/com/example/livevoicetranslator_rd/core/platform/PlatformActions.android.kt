package com.example.livevoicetranslator_rd.core.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import com.example.livevoicetranslator_rd.MainActivity
import com.example.livevoicetranslator_rd.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

actual class ClipboardService(private val context: Context) {

    actual fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }

    actual fun pasteFromClipboard(): String {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).coerceToText(context).toString()
        } else {
            ""
        }
    }
}

actual class ShareService(private val context: Context) {

    actual fun share(text: String, subject: String?) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(sendIntent, "Share").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }
}

actual class TTSService(private val context: Context) {

    private val tts: TextToSpeech by lazy {
        TextToSpeech(context) {}
    }

    actual suspend fun speak(
        text: String,
        lang: String?,
        rate: Float,
        gender: String?
    ) {
        withContext(Dispatchers.Main) {

            lang?.let {
                val locale = Locale(it)
                tts.language = locale
            }

            tts.setSpeechRate(rate)

            gender?.let {
                tts.voice = tts.voices.firstOrNull { voice ->
                    when (gender.lowercase()) {
                        "male" -> voice.name.contains("male", true)
                        "female" -> voice.name.contains("female", true)
                        else -> false
                    }
                } ?: tts.voice
            }

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts-id")
        }
    }
}

actual class AppUtilsService(private val activity: MainActivity) {
    actual fun shareApp() {
        val playStoreUrl = "https://play.google.com/store/apps/details?id=${activity.packageName}"
        val shareText = activity.getString(R.string.share_app_description, playStoreUrl)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        val shareToText = activity.getString(R.string.share_to)
        activity.startActivity(Intent.createChooser(shareIntent, shareToText))
    }

    actual fun rateApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${activity.packageName}"))
            activity.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}"))
            activity.startActivity(intent)
        }
    }

    actual fun contactUs(userType: String) {
        val contactEmail = activity.getString(R.string.contact_email)
        val appName = activity.getString(R.string.app_name)
        val emailSubject = activity.getString(R.string.email_subject, appName) + " user type $userType"

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(contactEmail))
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        }

        try {
            val sendMailText = activity.getString(R.string.send_mail)
            activity.startActivity(Intent.createChooser(emailIntent, sendMailText))
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$contactEmail?subject=${Uri.encode(emailSubject)}"))
            activity.startActivity(fallbackIntent)
        }
    }
}
