package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.PremiumButtonColor
import com.example.livevoicetranslator_rd.presentation.theme.PremiumIconColor
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_crown
import livevoicetranslatorrd.composeapp.generated.resources.pro
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumButton(
    modifier: Modifier = Modifier,
    label: String = stringResource(Res.string.pro),
    onClick: () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(PremiumButtonColor)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_crown),
                contentDescription = null,
                tint = PremiumIconColor,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = textStyle.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = PremiumIconColor
            )
        }
    }
}
