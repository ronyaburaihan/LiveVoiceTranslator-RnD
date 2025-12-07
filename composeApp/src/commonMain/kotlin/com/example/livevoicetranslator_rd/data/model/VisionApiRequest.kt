package com.example.livevoicetranslator_rd.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VisionApiRequest(
    val requests: List<AnnotateImageRequest>
)

@Serializable
data class AnnotateImageRequest(
    val image: Image,
    val features: List<Feature>
)

@Serializable
data class Image(
    val content: String? = null, // Base64 encoded image
    val source: Source? = null   // OR image URL
)

@Serializable
data class Source(
    val imageUri: String
)

@Serializable
data class Feature(
    val type: String = "TEXT_DETECTION",  // e.g., "TEXT_DETECTION"
    val maxResults: Int? = null  // optional
)