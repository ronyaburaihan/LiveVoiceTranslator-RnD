package com.example.livevoicetranslator_rd.domain.model

data class OCRResult(
    val fullText: String,
    val blocks: List<TextBlock>,
    val overallConfidence: Float,
    val detectedLanguage: String?,
    val engine: OCREngine
)

data class TextBlock(
    val text: String,
    val confidence: Float = 0.0f,
    val boundingBox: BoundingBox,
    val lines: List<TextLine> = emptyList(),
    val rotationAngle: Float = 0f
)

data class TextLine(
    val text: String,
    val confidence: Float,
    //val boundingBox: BoundingBox
)

data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

enum class OCREngine {
    ML_KIT_LATIN,
    ML_KIT_MULTILANG,
    GOOGLE_CLOUD_VISION,
    VISION
}