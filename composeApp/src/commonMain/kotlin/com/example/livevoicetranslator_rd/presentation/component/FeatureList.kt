package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.dividerColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FeatureList(
    features: List<Pair<DrawableResource, String>>,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    iconColor: Color = PrimaryColor
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        features.forEachIndexed { index, feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(feature.first),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(iconColor),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = feature.second,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }

            if (index != features.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = dividerColor)
            }
        }
    }
}