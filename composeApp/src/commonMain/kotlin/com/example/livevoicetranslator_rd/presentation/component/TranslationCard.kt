package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_copy_outline
import livevoicetranslatorrd.composeapp.generated.resources.ic_edit
import livevoicetranslatorrd.composeapp.generated.resources.ic_star
import livevoicetranslatorrd.composeapp.generated.resources.ic_volume_outline
import org.jetbrains.compose.resources.painterResource


@Composable
fun TranslationCard(
    sourceText: String,
    translatedText: String,
    accentColor: Color,
    isLeftAccent: Boolean = true,
    onSpeakClick: () -> Unit,
    onEditClick: () -> Unit,
    onSavedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(
        topStart = if (isLeftAccent) 2.dp else 8.dp,
        bottomStart = if (isLeftAccent) 2.dp else 8.dp,
        topEnd = if (isLeftAccent) 8.dp else 2.dp,
        bottomEnd = if (isLeftAccent) 8.dp else 2.dp,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(
                shape
            )
            .background(Color.White)
            .border(
                BorderStroke(1.dp, Color(0xFFEAEAEA)),
                shape = shape
            )
    ) {
        if (isLeftAccent) {
            AccentBar(accentColor, true)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = sourceText,
                    color = Color(0xFF6A6A6A),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(37.dp))

                GeneralActionButton(
                    icon = painterResource(Res.drawable.ic_edit),
                    contentDescription = "Edit Translation",
                    onClick = onEditClick,
                    color = Color.Unspecified,
                    iconSize = 14.dp,
                    boxSize = 22.dp
                )

                GeneralActionButton(
                    icon = painterResource(Res.drawable.ic_star),
                    contentDescription = "Save Translation",
                    onClick = onSavedClick,
                    color = Color.Unspecified,
                    iconSize = 14.dp,
                    boxSize = 22.dp
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = translatedText,
                    color = accentColor,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(33.dp))

                GeneralActionButton(
                    icon = painterResource(Res.drawable.ic_volume_outline),
                    contentDescription = "Read aloud",
                    onClick = { onSpeakClick() },
                    iconSize = 16.dp,
                    boxSize = 24.dp
                )
            }
        }

        if (!isLeftAccent) {
            AccentBar(accentColor, false)
        }
    }
}

@Composable
private fun AccentBar(color: Color, left: Boolean) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .fillMaxHeight()
            .clip(
                RoundedCornerShape(
                    topStart = if (left) 2.dp else 0.dp,
                    bottomStart = if (left) 2.dp else 0.dp,
                    topEnd = if (!left) 2.dp else 0.dp,
                    bottomEnd = if (!left) 2.dp else 0.dp,
                )
            )
            .background(color)
    )
}