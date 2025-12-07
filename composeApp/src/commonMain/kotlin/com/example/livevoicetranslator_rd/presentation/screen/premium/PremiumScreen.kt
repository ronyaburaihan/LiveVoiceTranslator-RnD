package com.example.livevoicetranslator_rd.presentation.screen.premium

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.FeatureList
import com.example.livevoicetranslator_rd.presentation.component.PrimaryButton
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.billingDescriptionColor
import com.example.livevoicetranslator_rd.presentation.theme.defaultCardBorder
import com.example.livevoicetranslator_rd.presentation.theme.featureBackground
import com.example.livevoicetranslator_rd.presentation.theme.headerBrush
import com.example.livevoicetranslator_rd.presentation.theme.monthlyBackground
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import com.example.livevoicetranslator_rd.presentation.theme.yearlyBackground
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
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
import livevoicetranslatorrd.composeapp.generated.resources.ic_monthly
import livevoicetranslatorrd.composeapp.generated.resources.ic_offline_translation
import livevoicetranslatorrd.composeapp.generated.resources.ic_tag
import livevoicetranslatorrd.composeapp.generated.resources.ic_voice_custom
import livevoicetranslatorrd.composeapp.generated.resources.ic_yearly
import livevoicetranslatorrd.composeapp.generated.resources.img_premium_background
import livevoicetranslatorrd.composeapp.generated.resources.premium_billing_description
import livevoicetranslatorrd.composeapp.generated.resources.start_free_trial
import livevoicetranslatorrd.composeapp.generated.resources.unlock_pro_access
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumScreen() {
    var selectedPlan by remember { mutableStateOf("yearly") }
    val navController = LocalNavController.current

    PremiumScreenContent(
        selectedPlan = selectedPlan,
        onPlanSelected = { plan -> selectedPlan = plan },
        onStartTrial = { /* Handle trial start */ },
        onCloseClick = { navController.navigateUp() }
    )
}

@Composable
fun PremiumScreenContent(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    onStartTrial: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val scrollState = rememberScrollState()
        var contentHeight by remember { mutableStateOf(0) }
        val scrollNeeded = contentHeight.dp > maxHeight
        val portraitBoxHeight = maxHeight * 0.20f
        val adaptiveBoxHeight = if (maxWidth > maxHeight) maxHeight * 0.5f else portraitBoxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .then(
                    if (scrollNeeded) Modifier.verticalScroll(scrollState)
                    else Modifier
                )
                .onSizeChanged { contentHeight = it.height }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(adaptiveBoxHeight)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(headerBrush)
                    .clipToBounds()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        IconButton(
                            onClick = onCloseClick,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }

                        Text(
                            text = stringResource(Res.string.unlock_pro_access),
                            color = MaterialTheme.colorScheme.surface,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(23.dp))
                    Image(
                        painter = painterResource(Res.drawable.img_premium_background),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                            .graphicsLayer(
                                scaleX = 1.5f,
                                scaleY = 1.5f
                            )
                            .offset(y = (-5).dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Features
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(featureBackground),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
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
                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                        .background(featureBackground)
                    , textColor = textColour
                )

            }

            Spacer(modifier = Modifier.height(19.dp))


            PricingPlan(
                title = "Monthly Plan",
                price = "3.99",
                isYearly = false,
                isSelected = selectedPlan == "monthly",
                onClick = { onPlanSelected("monthly") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            PricingPlan(
                title = "Yearly Plan",
                price = "29.99",
                isYearly = true,
                isSelected = selectedPlan == "yearly",
                onClick = { onPlanSelected("yearly") },
                offer = "60% OFF"
            )


            Spacer(modifier = Modifier.height(40.dp))

            Spacer(modifier = Modifier.weight(1f))

            // Start Free Trial Button
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.Center
            ) {
                PrimaryButton(
                    label = stringResource(Res.string.start_free_trial),
                    onClick = onStartTrial
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Billing description
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.premium_billing_description),
                    color = billingDescriptionColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun PricingPlan(
    title: String,
    price: String,
    isYearly: Boolean,
    isSelected: Boolean,
    offer: String? = null,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(8.dp),
            border = if (isSelected) BorderStroke(2.dp, PrimaryColor) else BorderStroke(
                2.dp, defaultCardBorder
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = if (isYearly)
                        painterResource(Res.drawable.ic_yearly)
                    else painterResource(Res.drawable.ic_monthly)

                    Box(
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isYearly) {
                                    yearlyBackground
                                } else {
                                    monthlyBackground
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }


                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = textColour
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "USD ",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                            color = textColour
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = price,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = textColour
                        )
                    }
                }
            }
        }
        offer?.let {
            OfferTag(label = it, modifier = Modifier.align(Alignment.TopEnd))
        }
    }

}


@Composable
fun OfferTag(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .offset(x = (-30).dp, y = (-14).dp)
            .paint(
                painterResource(Res.drawable.ic_tag),
                contentScale = ContentScale.Fit
            ),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
