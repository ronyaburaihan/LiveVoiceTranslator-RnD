package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import com.hashtag.generator.ai.post.writer.presentation.theme.TurnerAppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    label: String,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    leftIcon: DrawableResource? = null,
    rightIcon: DrawableResource? = null,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
    containerBrush: Brush = PrimaryBrush,
    buttonHeight: Dp = dimens.buttonHeight,
    cornerRadius: Dp = dimens.cornerRadiusSmall,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .heightIn(min = buttonHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (enabled) Modifier.background(containerBrush)
                else Modifier.background(disabledBackground)
            )
    ) {
        Button(
            modifier = Modifier.matchParentSize(),
            shape = RoundedCornerShape(cornerRadius),
            colors = ButtonDefaults.buttonColors(
                contentColor = contentColor,
                containerColor = Color.Transparent
            ),
            onClick = {
                if (!isLoading) {
                    onClick.invoke()
                }
            },
            enabled = enabled,
            content = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimens.iconSize),
                        color = contentColor,
                        strokeWidth = 2.dp
                    )
                } else {
                    leftIcon?.let {
                        Icon(
                            painter = painterResource(leftIcon),
                            contentDescription = null,
                            modifier = Modifier.size(dimens.smallIconSize)
                                .padding(2.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )

                    rightIcon?.let {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(rightIcon),
                            contentDescription = null,
                            modifier = Modifier.size(dimens.smallIconSize)
                                .padding(2.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    TurnerAppTheme {
        PrimaryButton(
            label = "Primary Button",
        ) {

        }
    }
}