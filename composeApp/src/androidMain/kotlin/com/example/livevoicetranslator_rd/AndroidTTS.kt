package com.example.livevoicetranslator_rd

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.livevoicetranslator_rd.presentation.screen.phrases.TTS
import java.util.Locale

class AndroidTTS(context: Context) : TTS {

    private val tts: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle unsupported language if needed
            }
        }
    }

    override fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}

