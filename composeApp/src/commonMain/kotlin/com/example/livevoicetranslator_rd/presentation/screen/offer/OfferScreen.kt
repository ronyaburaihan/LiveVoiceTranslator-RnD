package com.example.livevoicetranslator_rd.presentation.screen.offer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.FeatureList
import com.example.livevoicetranslator_rd.presentation.component.PrimaryButton
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.billingDescriptionColor2
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.theme.headerBrush
import com.example.livevoicetranslator_rd.presentation.theme.premiumTextColor
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.days_free_trial
import livevoicetranslatorrd.composeapp.generated.resources.feature_1
import livevoicetranslatorrd.composeapp.generated.resources.feature_2
import livevoicetranslatorrd.composeapp.generated.resources.feature_3
import livevoicetranslatorrd.composeapp.generated.resources.feature_4
import livevoicetranslatorrd.composeapp.generated.resources.feature_5
import livevoicetranslatorrd.composeapp.generated.resources.feature_6
import livevoicetranslatorrd.composeapp.generated.resources.feature_7
import livevoicetranslatorrd.composeapp.generated.resources.ic_ad__free
import livevoicetranslatorrd.composeapp.generated.resources.ic_camera_mini
import livevoicetranslatorrd.composeapp.generated.resources.ic_history
import livevoicetranslatorrd.composeapp.generated.resources.ic_language
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic
import livevoicetranslatorrd.composeapp.generated.resources.ic_offline_translation
import livevoicetranslatorrd.composeapp.generated.resources.ic_premium_golden
import livevoicetranslatorrd.composeapp.generated.resources.ic_voice_custom
import livevoicetranslatorrd.composeapp.generated.resources.img_mic
import livevoicetranslatorrd.composeapp.generated.resources.offer_title
import livevoicetranslatorrd.composeapp.generated.resources.premium_billing_description
import livevoicetranslatorrd.composeapp.generated.resources.start_free_trial
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OfferScreen() {
    val navController = LocalNavController.current

    OfferScreenContent(onClose = { navController.navigateUp() }, onStartTrial = {})

}

@Composable
fun OfferScreenContent(
    onClose: () -> Unit = {}, onStartTrial: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize().background(
            headerBrush
        ).navigationBarsPadding()
    ) {
        Box(modifier = Modifier.matchParentSize()) {
            Image(
                painter = painterResource(Res.drawable.img_mic),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .offset(y = (-60).dp, x = (-150).dp),   // move image
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.matchParentSize()
                    .background(headerBrush, alpha = 0.9f)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .statusBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 60.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_premium_golden),
                        contentDescription = null,
                        modifier = Modifier.size(74.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.offer_title),
                        style = MaterialTheme.typography.displaySmall,
                        color = premiumTextColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    val premiumFeatures = listOf(
                        Res.drawable.ic_mic to stringResource(Res.string.feature_1),
                        Res.drawable.ic_camera_mini to stringResource(Res.string.feature_2),
                        Res.drawable.ic_language to stringResource(Res.string.feature_3),
                        Res.drawable.ic_offline_translation to stringResource(Res.string.feature_4),
                        Res.drawable.ic_voice_custom to stringResource(Res.string.feature_5),
                        Res.drawable.ic_history to stringResource(Res.string.feature_6),
                        Res.drawable.ic_ad__free to stringResource(Res.string.feature_7),
                    )

                    FeatureList(
                        features = premiumFeatures,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                        iconColor = MaterialTheme.colorScheme.surface,

                        )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.days_free_trial),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        label = stringResource(Res.string.start_free_trial),
                        onClick = onStartTrial,
                        disabledBackground = MaterialTheme.colorScheme.surface,
                        cornerRadius = dimens.cornerRadiusSmall,
                        contentColor = PrimaryColor,
                        containerBrush = SolidColor(MaterialTheme.colorScheme.surface)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.premium_billing_description),
                        color = billingDescriptionColor2,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPremiumTrialScreen() {
    MaterialTheme {
        OfferScreenContent()
    }
}
