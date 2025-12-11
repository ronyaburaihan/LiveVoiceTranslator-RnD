package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.TranscriptState
import com.example.livevoicetranslator_rd.presentation.component.LanguageDropdownConversation
import com.example.livevoicetranslator_rd.presentation.component.MicButton
import com.example.livevoicetranslator_rd.presentation.component.TranslationCard
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic_default_conversation
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


val GoogleBlue = Color(0xFF4285F4)
val GoogleGreen = Color(0xFF34A853)
val LightGrayBg = Color(0xFFF8F9FA)
val GrayText = Color(0xFF757575)

@Composable
fun ConversationScreen() {
    val viewModel: ConversationViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val transcriptState by viewModel.transcriptState.collectAsState()

    ConversationScreenContent(
        uiState = uiState,
        liveText = uiState.liveText,
        statusText = uiState.statusText,
        targetLanguageCode = uiState.targetLanguageCode,
        transcriptState = transcriptState,
        onLeftMicClick = viewModel::onLeftMicClick,
        onRightMicClick = viewModel::onRightMicClick,
        onLeftMicLongPress = viewModel::onLeftMicLongPress,
        onRightMicLongPress = viewModel::onRightMicLongPress,
        onLeftLanguageSelected = viewModel::onLeftLanguageSelected,
        onRightLanguageSelected = viewModel::onRightLanguageSelected,
        onCopyClick = viewModel::onClickCopy,
        onDismissRequest = viewModel::onDismissRequest,
        openAppSettings = viewModel::openAppSettings,
        onUiEventHandled = viewModel::onUiEventHandled,
        onClearConversation = viewModel::clearConversation,
        onSpeakClick = viewModel::speak
    )

    // Handle UI events
    val uiEvent by viewModel.uiEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    uiEvent?.let { event ->
        when (event) {
            is ConversationViewModel.UiEvent.ShowSnackbar -> {
                LaunchedEffect(event) {
                    snackbarHostState.showSnackbar(event.message)
                    viewModel.onUiEventHandled()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationScreenContent(
    uiState: ConversationViewModel.ConversationUiState,
    liveText: String,
    statusText: String,
    targetLanguageCode: String,
    transcriptState: TranscriptState,
    onLeftMicClick: () -> Unit,
    onRightMicClick: () -> Unit,
    onLeftMicLongPress: () -> Unit,
    onRightMicLongPress: () -> Unit,
    onLeftLanguageSelected: (String) -> Unit,
    onRightLanguageSelected: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    openAppSettings: () -> Unit,
    onUiEventHandled: () -> Unit,
    onClearConversation: () -> Unit,
    onSpeakClick: (String, String) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(LightGrayBg)
        ) {
            val density = LocalDensity.current
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val centerOffset = Offset(
                    x = constraints.maxWidth / 2f,
                    y = constraints.maxHeight / 2f - with(density) { 52.dp.toPx() }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF237CCC).copy(alpha = 0.11f),
                                    Color.Transparent
                                ),
                                radius = with(density) { 240.dp.toPx() },
                                center = centerOffset
                            )
                        )
                )
            }

                ConversationList(
                    uiState = uiState,
                    transcriptState = transcriptState,
                    liveText = liveText,
                    targetLanguageCode = targetLanguageCode,
                    onSpeakClick = onSpeakClick,
                    onClearConversation = onClearConversation
                )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(101.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(75.dp),
                    color = Color.White,
                    shadowElevation = 16.dp,
                    content = {}
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ConversationMicWithLanguage(
                        modifier = Modifier.weight(1f),
                        isActive = uiState.isLeftMicActive,
                        micColor = GoogleBlue,
                        shadowColor = Color(0x4D0252FF),
                        language = uiState.leftLanguage,
                        availableLanguages = uiState.availableLanguages,
                        backgroundColor = Color(0xFFEFF4FF),
                        onMicClick = onLeftMicClick,
                        onLanguageSelected = onLeftLanguageSelected
                    )

                    ConversationMicWithLanguage(
                        modifier = Modifier.weight(1f),
                        isActive = uiState.isRightMicActive,
                        micColor = GoogleGreen,
                        shadowColor = Color(0x4D00B253),
                        language = uiState.rightLanguage,
                        availableLanguages = uiState.availableLanguages,
                        backgroundColor = Color(0xFF00B154).copy(0.10f),
                        onMicClick = onRightMicClick,
                        onLanguageSelected = onRightLanguageSelected
                    )
                }
            }

            if (transcriptState.showPermissionNeedDialog) {
                AlertDialog(
                    onDismissRequest = onDismissRequest,
                    title = { Text("Permission Required") },
                    text = { Text("This app needs microphone permission to use speech recognition. Please grant the permission in app settings.") },
                    confirmButton = {
                        TextButton(onClick = openAppSettings) {
                            Text("Open Settings")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDismissRequest) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ConversationList(
    uiState: ConversationViewModel.ConversationUiState,
    transcriptState: TranscriptState,
    liveText: String,
    targetLanguageCode: String,
    onSpeakClick: (String, String) -> Unit,
    onClearConversation: () -> Unit,
) {
    val messages = remember(uiState.messages) { uiState.messages.asReversed() }
    val showEmptyState = messages.isEmpty() && transcriptState.listeningStatus == ListeningStatus.INACTIVE

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {

            item(key = "bottomSpacer") {
                Spacer(modifier = Modifier.height(120.dp))
            }

            if (messages.isNotEmpty()) {
                item(key = "clearBtn") {
                    ClearConversationButton(onClearConversation)
                }
            }

            if (transcriptState.listeningStatus != ListeningStatus.INACTIVE &&
                !transcriptState.transcript.isNullOrEmpty()
            ) {
                item(key = "listeningCard") {
                    LiveListeningCard(liveText, uiState.isLeftMicActive)
                }
            }

            items(
                items = messages,
                key = { it.timestamp }
            ) { message ->
                TranslationCard(
                    sourceText = message.sourceText,
                    translatedText = message.translatedText,
                    accentColor = if (message.isLeftSide) GoogleBlue else GoogleGreen,
                    isLeftAccent = message.isLeftSide,
                    onSpeakClick = { onSpeakClick(message.translatedText, targetLanguageCode) },
                    onEditClick = {},
                    onSavedClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(key = "topSpacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }

        }

        if (showEmptyState) {
            EmptyConversationView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            )
        }
    }
}

@Composable
private fun ClearConversationButton(onClearConversation: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = onClearConversation,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
        ) {
            Icon(Icons.Default.ClearAll, null, Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Clear", fontSize = 12.sp)
        }
    }
}


@Composable
private fun LiveListeningCard(text: String, leftActive: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White.copy(0.7f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = if (leftActive) GoogleBlue else GoogleGreen
            )
            Spacer(Modifier.width(12.dp))
            Text(text, color = Color.Gray, fontSize = 16.sp, fontStyle = FontStyle.Italic)
        }
    }
}

@Composable
private fun EmptyConversationView(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        val iconSize = maxHeight * 0.2563f

        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_mic_default_conversation),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = Color.Unspecified
            )
            Spacer(Modifier.height(5.dp))
            Text("Start your conversation", color = Color(0xFF333333), style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(9.dp))
            Text(
                "Tap a microphone below and start speaking.",
                color = Color(0xFF777777),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ConversationMicWithLanguage(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    micColor: Color,
    shadowColor: Color,
    language: String,
    availableLanguages: List<String>,
    backgroundColor: Color,
    onMicClick: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MicButton(
            color = if (isActive) micColor.copy(alpha = 0.8f) else micColor,
            shadowColor = shadowColor,
            onClick = onMicClick,
            onLongClick = {},
            onLongClickRelease = {},
            modifier = Modifier.size(52.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        LanguageDropdownConversation(
            selectedLanguage = language,
            availableLanguages = availableLanguages,
            color = micColor,
            backgroundColor = backgroundColor,
            onLanguageSelected = onLanguageSelected
        )
    }
}
