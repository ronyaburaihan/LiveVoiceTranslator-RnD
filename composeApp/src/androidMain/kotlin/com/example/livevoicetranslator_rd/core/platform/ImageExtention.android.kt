package com.example.livevoicetranslator_rd.core.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
    return bitmap.asImageBitmap()
}

actual fun ByteArray.resizeAndCompress(
    maxWidth: Int,
    maxHeight: Int,
    quality: Int,
): ByteArray {
    // Decode into Bitmap
    val originalBitmap = BitmapFactory.decodeByteArray(this, 0, size)

    // Calculate aspect-ratio safe size
    val ratio = minOf(
        maxWidth.toFloat() / originalBitmap.width,
        maxHeight.toFloat() / originalBitmap.height
    )
    val newWidth = (originalBitmap.width * ratio).toInt()
    val newHeight = (originalBitmap.height * ratio).toInt()

    // Resize
    val resized = originalBitmap.scale(newWidth, newHeight)

    // Compress to JPEG (FIXED!)
    val stream = ByteArrayOutputStream()
    resized.compress(Bitmap.CompressFormat.JPEG, quality, stream)  // ← Changed from PNG to JPEG

    return stream.toByteArray()
}

actual fun ByteArray.encodeBase64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

actual fun String.decodeBase64(): ByteArray {
    return Base64.decode(this, Base64.NO_WRAP)
}