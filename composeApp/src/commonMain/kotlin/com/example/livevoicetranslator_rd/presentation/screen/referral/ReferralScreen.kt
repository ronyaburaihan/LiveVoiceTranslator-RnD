package com.example.livevoicetranslator_rd.presentation.screen.referral

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.component.AppTopBar
import com.example.livevoicetranslator_rd.presentation.component.AppTopBarTitle
import com.example.livevoicetranslator_rd.presentation.component.CustomSlider
import com.example.livevoicetranslator_rd.presentation.component.PrimaryButton
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_copy_referral
import livevoicetranslatorrd.composeapp.generated.resources.ic_gift_box
import livevoicetranslatorrd.composeapp.generated.resources.ic_or
import livevoicetranslatorrd.composeapp.generated.resources.referral__copy_button
import livevoicetranslatorrd.composeapp.generated.resources.referral__subtitle
import livevoicetranslatorrd.composeapp.generated.resources.referral__title
import livevoicetranslatorrd.composeapp.generated.resources.referrals
import livevoicetranslatorrd.composeapp.generated.resources.total_earnings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReferralScreen() {
    val navController = LocalNavController.current

    ReferralScreenContent(
        modifier = Modifier,
        referralCount = 0,
        referralUrl = "www.playstore.com/livevoicetranslator_rd",
        canMakeReferrals = true,
        onBackClick = {navController.navigateUp()},
        onCopyClick = {},
    )
}

@Composable
fun ReferralScreenContent(
    modifier: Modifier = Modifier,
    referralCount: Int,
    referralUrl: String,
    canMakeReferrals: Boolean,
    onBackClick: () -> Unit,
    onCopyClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopBar(
                title = {
                    AppTopBarTitle(
                        title = "Referral"
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
                }
            )
        }
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(Res.drawable.ic_gift_box),
                    modifier = Modifier.size(170.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.referral__title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.referral__subtitle),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.heightIn(min = 12.dp))
            // Referral Link Section

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                if (referralUrl.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(0.80f)
                            ) {
                                Text(
                                    text = referralUrl,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(
                                modifier = Modifier.width(15.dp)
                            )
                            Box(
                                modifier = Modifier.weight(0.10f)
                            ) {
                                IconButton(
                                    onClick = onCopyClick
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_copy_referral),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier.weight(0.40f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_or),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier.weight(0.40f)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                label = stringResource(Res.string.referral__copy_button),
                onClick = {
//                    val shareIntent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        type = "text/plain"
//                        val message =
//                            context.getString(com.improvetype.ai.keyboard.R.string.refer_link_copy_text, referralUrl)
//                        putExtra(
//                            Intent.EXTRA_TEXT,
//                            message
//                        )
//                    }
//                    context.startActivity(Intent.createChooser(shareIntent, "Share Referral Link"))
                },
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Referral Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(Res.string.total_earnings),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
//                        Text(
//                            text = "${referralCount * 3000} " + stringResource(R.string.words),
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.onBackground
//                        )

                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(Res.string.referrals),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr){
                            Text(
                                text = "$referralCount of 7",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomSlider(
                        modifier = Modifier.padding(5.dp),
                        trackHeight = 11.dp,
                        thumbSize = 19.dp,
                        valueRange = 0f..7f,
                        onValueChange = {},
                        progress = referralCount.toFloat(),
                        onProgressChange = {},
                    )
                }


            }

            Spacer(modifier = Modifier.height(24.dp))

//            if (!canMakeReferrals) {
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Card(
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.errorContainer
//                    )
//                ) {
//                    Text(
//                        text = stringResource(Res.string.max_referral_text),
//                        modifier = Modifier.padding(16.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onErrorContainer,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
        }
    }

}
