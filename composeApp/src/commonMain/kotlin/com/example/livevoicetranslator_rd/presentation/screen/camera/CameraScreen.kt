package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.core.platform.ImagePicker
import com.example.livevoicetranslator_rd.core.platform.toImageBitmap
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_upload
import org.jetbrains.compose.resources.painterResource

@Composable
fun CameraScreen() {

    val imagePicker = remember { ImagePicker() }
    var pickedImage by remember { mutableStateOf<ByteArray?>(null) }

    imagePicker.RegisterPicker { imageBytes ->
        pickedImage = imageBytes
        println("Compressed bytes: ${pickedImage?.size}")
    }

    CameraScreenContent(
        pickedImage = pickedImage,
        onPickImage = imagePicker::pickImage,
    )
}

@Composable
private fun CameraScreenContent(
    pickedImage: ByteArray?,
    onPickImage: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.horizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(dimens.verticalPadding))

            ImagePickerLayout(
                modifier = Modifier.fillMaxWidth(),
                title = "Pick Image",
                imageBitmap = pickedImage?.toImageBitmap(),
                onPickImage = onPickImage
            )
        }
    }
}

@Composable
fun ImagePickerLayout(
    modifier: Modifier = Modifier,
    title: String,
    imageBitmap: ImageBitmap? = null,
    onPickImage: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageBitmap?.let {
                Image(
                    modifier = Modifier.height(100.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(dimens.cornerRadiusSmall)),
                    bitmap = imageBitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(dimens.spaceBetween))
            }
            Column(
                modifier = Modifier.weight(1f)
                    .heightIn(min = 100.dp)
                    .clip(RoundedCornerShape(dimens.cornerRadiusSmall))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onPickImage.invoke() }
                    .drawBehind {
                        val stroke = Stroke(
                            width = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                        drawRoundRect(
                            brush = PrimaryBrush,
                            style = stroke,
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }.padding(dimens.contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_upload),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Click here to select a photo",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
