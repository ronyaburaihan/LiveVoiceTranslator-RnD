package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_hambuger
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomIconButton(
    icon: DrawableResource = Res.drawable.ic_hambuger,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier.size(dimens.iconSize),
                painter = painterResource(icon),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.surface
            )
        }
    )
}


