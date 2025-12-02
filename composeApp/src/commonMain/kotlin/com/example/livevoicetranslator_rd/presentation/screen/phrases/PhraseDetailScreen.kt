package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.component.AppTopBar
import com.example.livevoicetranslator_rd.presentation.component.AppTopBarTitle
import com.example.livevoicetranslator_rd.presentation.component.LanguageDropdown
import com.example.livevoicetranslator_rd.presentation.component.PhraseCard
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_swap
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhraseDetailScreen(
    categoryTitle: String
) {
    val navController = LocalNavController.current
    val tts = rememberTTS()


    val phrases = remember {
        mutableStateListOf(
            PhraseItem(
                1,
                "Hello I am talking now what's the",
                "Hola, estoy hablando ahora, qué?",
                isExpanded = true
            ),
            PhraseItem(2, "I like learning new languages.", "Me gusta aprender nuevos idiomas."),
            PhraseItem(3, "Can you help me with this?", "¿Puedes ayudarme con esto?"),
            PhraseItem(4, "The weather is very nice today.", "El clima está muy agradable hoy.")
        )
    }
    PhraseDetailScreenContent(
        categoryName = categoryTitle,
        onBackClick = { navController.navigateUp() },
        phrases = phrases,
        tts = tts
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhraseDetailScreenContent(
    categoryName: String,
    onBackClick: () -> Unit = {},
    phrases: SnapshotStateList<PhraseItem>,
    tts: TTS
) {
    var selectedSourceLanguage by remember { mutableStateOf("English") }
    var selectedTargetLanguage by remember { mutableStateOf("Spanish") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopBar(
                title = {
                    AppTopBarTitle(
                        title = categoryName
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        content = {
                            Icon(
                                modifier = Modifier.size(dimens.iconSize),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Source Language
                        LanguageDropdown(
                            language = selectedSourceLanguage,
                            onLanguageChange = { selectedSourceLanguage = it }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // Swap Button
                        Image(
                            painter = painterResource(Res.drawable.ic_swap),
                            contentDescription = "Swap",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    val temp = selectedSourceLanguage
                                    selectedSourceLanguage = selectedTargetLanguage
                                    selectedTargetLanguage = temp
                                }
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        // Target Language
                        LanguageDropdown(
                            language = selectedTargetLanguage,
                            onLanguageChange = { selectedTargetLanguage = it }
                        )

                        // Search Button
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(phrases) { phrase ->
                PhraseCard(
                    phrase = phrase,
                    onExpandClick = {
                        val index = phrases.indexOf(phrase)
                        phrases[index] = phrase.copy(isExpanded = !phrase.isExpanded)
                    },
                    onFavoriteClick = {
                        val index = phrases.indexOf(phrase)
                        phrases[index] = phrase.copy(isFavorite = !phrase.isFavorite)
                    },
                    tts = tts
                )
            }
        }
    }
}