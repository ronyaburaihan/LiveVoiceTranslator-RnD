package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.BackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.borderColor
import com.example.livevoicetranslator_rd.presentation.theme.boxColor
import com.example.livevoicetranslator_rd.presentation.theme.iconBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.premiumBackgroundColor
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_premium
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    title: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundColor
            ),
            border = BorderStroke(1.dp, borderColor),
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(0.4.dp, borderColor, RoundedCornerShape(8.dp))
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))

                // Icon background
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = boxColor),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(1.dp)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF303133)
                )
            }
        }
        // PRO badge (top-right)
        if (isLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-5).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color = premiumBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_premium),
                    contentDescription = "Pro",
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}