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


val GoogleBlue = Color(0xFF4285F4)
val GoogleGreen = Color(0xFF34A853)
val LightGrayBg = Color(0xFFF8F9FA)
val GrayText = Color(0xFF757575)

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(
                RoundedCornerShape(
                    topStart = if (isLeftAccent) 2.dp else 8.dp,
                    bottomStart = if (isLeftAccent) 2.dp else 8.dp,
                    topEnd = if (isLeftAccent) 8.dp else 2.dp,
                    bottomEnd = if (isLeftAccent) 8.dp else 2.dp,
                )
            )
            .background(Color.White)
            .border(
                BorderStroke(1.dp, Color(0xFFEAEAEA)),
                shape = RoundedCornerShape(
                    topStart = if (isLeftAccent) 2.dp else 8.dp,
                    bottomStart = if (isLeftAccent) 2.dp else 8.dp,
                    topEnd = if (isLeftAccent) 8.dp else 2.dp,
                    bottomEnd = if (isLeftAccent) 8.dp else 2.dp,
                )
            )
    ) {
        if (isLeftAccent) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(accentColor)
            )
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
                    color = GrayText,
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

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFEEEEEE))

            Spacer(modifier = Modifier.height(12.dp))

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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(accentColor)
            )
        }
    }
}
