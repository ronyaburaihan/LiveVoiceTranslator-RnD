package com.example.livevoicetranslator_rd.core.platform

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect fun ByteArray.resizeAndCompress(
    maxWidth: Int,
    maxHeight: Int,
    quality: Int = 80
): ByteArray

expect fun ByteArray.encodeBase64(): String

expect fun String.decodeBase64(): ByteArray