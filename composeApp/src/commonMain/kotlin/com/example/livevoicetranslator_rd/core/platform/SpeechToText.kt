package com.example.livevoicetranslator_rd.core.platform



import com.example.livevoicetranslator_rd.domain.model.speachtotext.PermissionRequestStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.Transcript
import com.example.livevoicetranslator_rd.domain.model.speachtotext.TranscriptState
import kotlinx.coroutines.flow.MutableStateFlow

expect class SpeechToText {
    val transcriptState: MutableStateFlow<TranscriptState>
    fun startTranscribing()
    fun stopTranscribing()
    fun requestPermission(onPermissionResult: (PermissionRequestStatus) -> Unit)
    fun getSupportedLanguages(onLanguagesResult: (List<String>) -> Unit)
    fun setLanguage(languageCode: String)
    fun copyText(text: String)
    fun showNeedPermission()
    fun dismissPermissionDialog()
    fun openAppSettings()
}