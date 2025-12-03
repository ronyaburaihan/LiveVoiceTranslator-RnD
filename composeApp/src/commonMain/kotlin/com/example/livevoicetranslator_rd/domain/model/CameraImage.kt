package com.example.livevoicetranslator_rd.domain.model

data class CameraImage(
    val imageData: ByteArray,
    val width: Int,
    val height: Int,
    val rotation: Int = 0,
    val source: ImageSource
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as CameraImage
        return imageData.contentEquals(other.imageData) &&
                width == other.width &&
                height == other.height
    }

    override fun hashCode(): Int {
        var result = imageData.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}