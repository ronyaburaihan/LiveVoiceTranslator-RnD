package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.billingDescriptionColor
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.theme.premiumBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.cancel_anytime_description
import livevoicetranslatorrd.composeapp.generated.resources.ic_crown
import livevoicetranslatorrd.composeapp.generated.resources.limit_reached_offer_description
import livevoicetranslatorrd.composeapp.generated.resources.limit_reached_offer_title
import livevoicetranslatorrd.composeapp.generated.resources.upgrade_now_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UnlockPremiumScreen() {
    val navController = LocalNavController.current
    UnlockPremiumCard(
        onClose = { navController.navigateUp() },
        onUpgrade = {navController.navigate(ScreenRoute.Premium)}
    )

}

@Composable
fun UnlockPremiumCard(onClose: () -> Unit, onUpgrade: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(30.dp))
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onClose, modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = textColour
                        )
                    }
                }
                Box(
                    modifier = Modifier.size(width = 64.dp, height = 50.dp)
                        .clip(RoundedCornerShape(20.dp)).background(
                            premiumBackgroundColor
                        )
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_crown),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(textColour),
                        modifier = Modifier.size(32.dp).align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(Res.string.limit_reached_offer_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = textColour
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(Res.string.limit_reached_offer_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColour,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    label = stringResource(Res.string.upgrade_now_button),
                    disabledBackground = PrimaryColor,
                    buttonHeight = dimens.buttonHeight,
                    cornerRadius = dimens.cornerRadiusSmall,
                    onClick = { onUpgrade() })
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.cancel_anytime_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = billingDescriptionColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockPremiumCardPreview() {
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Blue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UnlockPremiumCard(onClose = {}, onUpgrade = {})
    }
}

