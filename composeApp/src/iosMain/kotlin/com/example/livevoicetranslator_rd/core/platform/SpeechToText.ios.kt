package com.example.livevoicetranslator_rd.core.platform


import com.example.livevoicetranslator_rd.domain.model.speachtotext.Error
import com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.PermissionRequestStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.RecognizerError
import com.example.livevoicetranslator_rd.domain.model.speachtotext.TranscriptState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import platform.AVFAudio.*
import platform.AVFAudio.setActive
import platform.AVFAudio.setPreferredIOBufferDuration
import platform.AVFAudio.setPreferredSampleRate
import platform.Foundation.NSError
import platform.Foundation.NSLocale
import platform.Foundation.NSURL
import platform.Foundation.localeIdentifier
import platform.Speech.*
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIPasteboard
import kotlin.apply
import kotlin.collections.mapNotNull
import kotlin.coroutines.resume
import kotlin.let
import kotlin.runCatching
import kotlin.text.contains

actual class SpeechToText {

    // State management
    private val _transcriptState = MutableStateFlow(
        TranscriptState(
            listeningStatus = ListeningStatus.INACTIVE,
            error = Error(isError = false),
            transcript = null,
        )
    )

    actual val transcriptState: MutableStateFlow<TranscriptState>
        get() = _transcriptState

    // Audio components with proper lifecycle management
    private var audioEngine: AVAudioEngine? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private var speechRecognizer: SFSpeechRecognizer? = null

    // State flags
    private var isTapInstalled = false
    private var isCurrentlyTranscribing = false
    private var hasRetriedNoSpeech = false

    // Thread-safe operations
    private val transcriptionMutex = Mutex()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        // Initialize on Main thread to be safe with iOS APIs
        scope.launch(Dispatchers.Main) {
            initializeRecognizer()
            loadSupportedLanguages()
        }
    }

    private fun initializeRecognizer() {
        speechRecognizer = SFSpeechRecognizer()
    }

    private fun loadSupportedLanguages() {
        getSupportedLanguages { supportedLanguages ->
            _transcriptState.update { it.copy(supportedLanguages = supportedLanguages) }
        }
    }

    actual fun startTranscribing() {
        scope.launch {
            transcriptionMutex.withLock {
                if (isCurrentlyTranscribing) {
                    println("SpeechToText.iOS: Already transcribing, ignoring start request")
                    return@launch
                }

                // Check permissions first
                val currentRecordPermission = AVAudioSession.sharedInstance().recordPermission
                val currentSpeechPermission = SFSpeechRecognizer.authorizationStatus()

                println("SpeechToText.iOS: Checking permissions before transcribing - Record: $currentRecordPermission, Speech: $currentSpeechPermission")
                
                // If permissions are not granted, request them
                if (currentRecordPermission != AVAudioSessionRecordPermissionGranted ||
                    currentSpeechPermission != SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {

                    println("SpeechToText.iOS: Permissions not granted, requesting...")
                    handleTranscriptionError("Microphone or speech recognition permission not granted")
                    return@launch
                }

                val recognizer = speechRecognizer
                if (recognizer == null || !recognizer.isAvailable()) {
                    handleRecognizerUnavailable()
                    return@launch
                }

                try {
                    startTranscriptionSession(recognizer)
                } catch (e: Exception) {
                    handleTranscriptionError(e.message ?: "Unknown error starting transcription")
                }
            }
        }
    }

    private suspend fun startTranscriptionSession(recognizer: SFSpeechRecognizer) {
        println("SpeechToText.iOS: Starting transcription session")

        isCurrentlyTranscribing = true
        updateListeningStatus(ListeningStatus.LISTENING)

        try {
            withContext(Dispatchers.Main) {
                // 1. Configure Audio Session
                configureAudioSession()

                // 2. Create Request
                val request = SFSpeechAudioBufferRecognitionRequest().apply {
                    shouldReportPartialResults = true
                    requiresOnDeviceRecognition = false
                }
                recognitionRequest = request

                // 3. Create Task (but don't start engine yet)
                // Keep a reference to the task so we can cancel it if it fails immediately
                val task = recognizer.recognitionTaskWithRequest(request) { result, error ->
                    handleRecognitionResult(result, error)
                }
                recognitionTask = task

                // 4. Setup Audio Engine & Tap
                val engine = AVAudioEngine()
                audioEngine = engine
                
                val inputNode = engine.inputNode
                val recordingFormat = inputNode.outputFormatForBus(0u)
                
                if (recordingFormat.sampleRate == 0.0) {
                    throw IllegalStateException("Invalid audio format - sample rate is 0")
                }

                inputNode.installTapOnBus(0u, 1024u, recordingFormat) { buffer, _ ->
                    buffer?.let { 
                        request.appendAudioPCMBuffer(it) 
                    }
                }
                isTapInstalled = true
                println("SpeechToText.iOS: Audio tap installed")

                // 5. Start Engine
                startAudioEngine(engine)
            }
        } catch (e: Exception) {
            println("SpeechToText.iOS: Error in startTranscriptionSession: ${e.message}")
            isCurrentlyTranscribing = false
            // Ensure cleanup happens on Main thread if it failed here
            withContext(Dispatchers.Main) {
                cleanupResourcesInternal()
            }
            handleTranscriptionError("Failed to start audio engine: ${e.message}")
        }
    }

    private fun handleRecognitionResult(
        result: SFSpeechRecognitionResult?,
        error: NSError?
    ) {
        // This callback might not be on Main thread, but state updates are safe
        when {
            result != null -> {
                val transcript = result.bestTranscription.formattedString
                updateTranscriptSuccess(transcript)
                hasRetriedNoSpeech = false
            }
            error != null -> {
                handleRecognitionError(error)
            }
        }
    }

    private fun handleRecognitionError(error: NSError) {
        val errorMessage = error.localizedDescription
        val isNoSpeechError = errorMessage?.contains("No speech", ignoreCase = true) == true

        if (isNoSpeechError && !hasRetriedNoSpeech) {
            println("SpeechToText.iOS: No speech detected, retrying...")
            hasRetriedNoSpeech = true
            scope.launch {
                transcriptionMutex.withLock {
                    cleanupResources()
                    startTranscribing()
                }
            }
        } else {
            println("SpeechToText.iOS: Recognition error: $errorMessage")
            handleTranscriptionError(errorMessage)
            hasRetriedNoSpeech = false
        }
    }

    actual fun stopTranscribing() {
        scope.launch {
            transcriptionMutex.withLock {
                println("SpeechToText.iOS: Stopping transcription")
                cleanupResources()
                updateListeningStatus(ListeningStatus.INACTIVE)
                Error(isError = false, message = null)
            }
        }
    }

    private suspend fun cleanupResources() {
        withContext(Dispatchers.Main) {
            cleanupResourcesInternal()
        }
    }

    // Must be called from Main Thread
    @OptIn(ExperimentalForeignApi::class)
    private fun cleanupResourcesInternal() {
        println("SpeechToText.iOS: Cleaning up resources")

        // Stop recognition request
        recognitionRequest?.endAudio()

        // Cancel and cleanup recognition task
        recognitionTask?.let { task ->
            runCatching { task.finish() }
            runCatching { task.cancel() }
        }

        // Remove audio tap if installed
        audioEngine?.let { engine ->
            if (isTapInstalled) {
                runCatching {
                    engine.inputNode.removeTapOnBus(0u)
                    println("SpeechToText.iOS: Audio tap removed")
                }
                isTapInstalled = false
            }

            // Stop audio engine
            if (engine.running) {
                engine.stop()
                println("SpeechToText.iOS: Audio engine stopped")
            }
        }

        // Deactivate audio session
        runCatching {
            AVAudioSession.sharedInstance().setActive(
                false,
                1uL, // AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
                null
            )
            println("SpeechToText.iOS: Audio session deactivated")
        }

        // Clear references
        recognitionTask = null
        audioEngine = null
        recognitionRequest = null
        isCurrentlyTranscribing = false

        println("SpeechToText.iOS: Cleanup completed")
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun configureAudioSession() {
        val audioSession = AVAudioSession.sharedInstance()

        try {
            // Deactivate first to ensure clean state
            audioSession.setActive(false, null)
            
            // PlayAndRecord is generally safer for apps that might play sounds too.
            // DefaultToSpeaker ensures audio goes to speaker instead of receiver.
            val options = AVAudioSessionCategoryOptionDefaultToSpeaker or
                    AVAudioSessionCategoryOptionAllowBluetooth
            
            val categorySuccess = audioSession.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                AVAudioSessionModeSpokenAudio,
                options,
                null
            )

            if (!categorySuccess) {
                println("SpeechToText.iOS: Warning - Failed to set audio session category")
            }

            audioSession.setPreferredSampleRate(16000.0, null)
            audioSession.setPreferredIOBufferDuration(0.02, null)

            val activateSuccess = audioSession.setActive(
                true,
                1uL, // AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
                null
            )

            if (!activateSuccess) {
                println("SpeechToText.iOS: Warning - Failed to activate audio session")
            }
        } catch (e: Exception) {
            println("SpeechToText.iOS: Error configuring audio session: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startAudioEngine(engine: AVAudioEngine) {
        engine.prepare()
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val started = engine.startAndReturnError(errorPtr.ptr)

            if (started) {
                println("SpeechToText.iOS: Audio engine started successfully")
            } else {
                val error = errorPtr.value
                val errorMessage =
                    error?.localizedDescription ?: "Unknown error starting audio engine"
                println("SpeechToText.iOS: Failed to start audio engine - $errorMessage")
                throw IllegalStateException("Failed to start audio engine: $errorMessage")
            }
        }
    }

    actual fun requestPermission(onPermissionResult: (PermissionRequestStatus) -> Unit) {
        // Check current permission status and request if needed
        try {
            // First check current permission status without requesting
            val currentRecordPermission = AVAudioSession.sharedInstance().recordPermission
            val currentSpeechPermission = SFSpeechRecognizer.authorizationStatus()

            println("SpeechToText.iOS: Current permissions - Record: $currentRecordPermission, Speech: $currentSpeechPermission")
            
            // If permissions are already granted, return immediately
            if (currentRecordPermission == AVAudioSessionRecordPermissionGranted &&
                currentSpeechPermission == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                onPermissionResult(PermissionRequestStatus.ALLOWED)
                return
            }
            
            // If permissions are denied permanently, show dialog
            if (currentRecordPermission == AVAudioSessionRecordPermissionDenied ||
                currentSpeechPermission == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusDenied) {
                onPermissionResult(PermissionRequestStatus.NEVER_ASK_AGAIN)
                return
            }
            
            // For undetermined permissions, we need to request them
            // We'll request them immediately and return the result
            println("SpeechToText.iOS: Permissions undetermined, requesting...")
            
            // Request both permissions - this will trigger the system dialogs
            // We need to request them one by one since they're async
            
            // First request record permission
            AVAudioSession.sharedInstance().requestRecordPermission { recordGranted ->
                println("SpeechToText.iOS: Record permission result: $recordGranted")
                
                // Then request speech recognition permission
                SFSpeechRecognizer.requestAuthorization { speechStatus ->
                    val speechGranted = speechStatus == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized
                    println("SpeechToText.iOS: Speech permission result: $speechStatus")
                    
                    // Determine final status based on both permissions
                    val finalStatus = determinePermissionStatus(recordGranted, speechGranted)
                    println("SpeechToText.iOS: Final permission status: $finalStatus")
                    onPermissionResult(finalStatus)
                }
            }
            
        } catch (e: Exception) {
            println("SpeechToText.iOS: Error checking permissions: ${e.message}")
            onPermissionResult(PermissionRequestStatus.NOT_ALLOWED)
        }
    }

    private suspend fun checkRecordPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            AVAudioSession.sharedInstance().requestRecordPermission { granted ->
                if (continuation.isActive) {
                    continuation.resume(granted)
                }
            }
        }

    private suspend fun checkSpeechPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            SFSpeechRecognizer.requestAuthorization { status ->
                if (continuation.isActive) {
                    val isAuthorized = status ==
                            SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized
                    continuation.resume(isAuthorized)
                }
            }
        }

    private fun determinePermissionStatus(
        hasRecordPermission: Boolean,
        hasSpeechPermission: Boolean
    ): PermissionRequestStatus {
        return when {
            hasRecordPermission && hasSpeechPermission ->
                PermissionRequestStatus.ALLOWED

            isPermissionDenied() ->
                PermissionRequestStatus.NEVER_ASK_AGAIN

            else ->
                PermissionRequestStatus.NOT_ALLOWED
        }
    }

    private fun isPermissionDenied(): Boolean {
        val recordStatus = AVAudioSession.sharedInstance().recordPermission
        val speechStatus = SFSpeechRecognizer.authorizationStatus()

        return recordStatus == AVAudioSessionRecordPermissionDenied ||
                speechStatus == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusDenied
    }

    actual fun setLanguage(languageCode: String) {
        scope.launch {
            transcriptionMutex.withLock {
                val wasTranscribing = isCurrentlyTranscribing

                if (wasTranscribing) {
                    cleanupResources()
                }

                // Initialize on Main Thread for safety
                withContext(Dispatchers.Main) {
                    val locale = NSLocale(languageCode)
                    speechRecognizer = SFSpeechRecognizer(locale)
                    println("SpeechToText.iOS: Language set to $languageCode")
                }

                if (wasTranscribing) {
                    startTranscribing()
                }
            }
        }
    }

    actual fun getSupportedLanguages(onLanguagesResult: (List<String>) -> Unit) {
        val supportedLocales = SFSpeechRecognizer.supportedLocales()
        val languages = supportedLocales.mapNotNull {
            (it as? NSLocale)?.localeIdentifier()
        }
        onLanguagesResult(languages)
    }

    actual fun copyText(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }

    actual fun showNeedPermission() {
        _transcriptState.update { it.copy(showPermissionNeedDialog = true) }
    }

    actual fun dismissPermissionDialog() {
        _transcriptState.update { it.copy(showPermissionNeedDialog = false) }
    }

    actual fun openAppSettings() {
        val recordStatus = AVAudioSession.sharedInstance().recordPermission
        val speechStatus = SFSpeechRecognizer.authorizationStatus()

        when {
            recordStatus == AVAudioSessionRecordPermissionDenied ->
                openMicrophoneSettings()

            speechStatus == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusDenied ->
                openSpeechRecognitionSettings()

            else ->
                openGeneralSettings()
        }

        dismissPermissionDialog()
    }

    private fun openMicrophoneSettings() {
        val url = NSURL.URLWithString("prefs:root=Privacy&path=MICROPHONE")
        openURLOrFallback(url)
    }

    private fun openSpeechRecognitionSettings() {
        val url = NSURL.URLWithString("prefs:root=Privacy&path=SPEECH_RECOGNITION")
        openURLOrFallback(url)
    }

    private fun openURLOrFallback(url: NSURL?) {
        val app = UIApplication.sharedApplication

        if (url != null && app.canOpenURL(url)) {
            app.openURL(url, mapOf<Any?, Any>(), null)
        } else {
            openGeneralSettings()
        }
    }

    private fun openGeneralSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        url?.let { UIApplication.sharedApplication.openURL(it, mapOf<Any?, Any>(), null) }
    }

    // Helper methods for state updates
    private fun updateListeningStatus(status: ListeningStatus) {
        _transcriptState.update { it.copy(listeningStatus = status) }
    }

    private fun updateTranscriptSuccess(transcript: String) {
        _transcriptState.update {
            it.copy(
                transcript = transcript,
                error = Error(isError = false)
            )
        }
    }

    private fun handleTranscriptionError(message: String) {
        println("SpeechToText.iOS: Transcription error: $message")
        
        // Launch cleanup on correct context
        scope.launch {
            cleanupResources()
        }

        _transcriptState.update {
            it.copy(
                listeningStatus = ListeningStatus.INACTIVE,
                error = Error(isError = false, message = message),
                transcript = null
            )
        }
    }

    private fun handleRecognizerUnavailable() {
        _transcriptState.update {
            it.copy(
                listeningStatus = ListeningStatus.INACTIVE,
                error = Error(
                    isError = true,
                    message = RecognizerError.RecognizerIsUnavailable.message
                )
            )
        }
    }

    // Cleanup on deinitialization
    fun cleanup() {
        scope.launch {
            transcriptionMutex.withLock {
                cleanupResources()
            }
            scope.cancel()
        }
    }
}