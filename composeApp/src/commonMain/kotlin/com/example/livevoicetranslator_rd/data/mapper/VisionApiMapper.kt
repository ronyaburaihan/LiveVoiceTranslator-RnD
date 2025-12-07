package com.example.livevoicetranslator_rd.data.mapper

import com.example.livevoicetranslator_rd.data.model.AnnotateImageRequest
import com.example.livevoicetranslator_rd.data.model.AnnotateImageResponse
import com.example.livevoicetranslator_rd.data.model.BoundingPoly
import com.example.livevoicetranslator_rd.data.model.EntityAnnotation
import com.example.livevoicetranslator_rd.data.model.Feature
import com.example.livevoicetranslator_rd.data.model.Image
import com.example.livevoicetranslator_rd.data.model.TextAnnotation
import com.example.livevoicetranslator_rd.data.model.VisionApiRequest
import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.OCREngine
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.domain.model.TextLine
import kotlin.math.PI
import kotlin.math.atan2

fun String.toVisionApiRequest(): VisionApiRequest {

    val requestBody = VisionApiRequest(
        requests = listOf(
            AnnotateImageRequest(
                image = Image(content = this),
                features = listOf(
                    Feature(
                        type = "DOCUMENT_TEXT_DETECTION",
                        //maxResults = 1
                    )
                )
            )
        )
    )
    return requestBody
}

/**
 * Calculates rotation angle from Cloud Vision API bounding box vertices
 * Cloud Vision vertices order: [top-left, top-right, bottom-right, bottom-left]
 * Returns angle in degrees (positive = clockwise rotation)
 */
private fun calculateRotationAngleFromCloudVision(boundingPoly: BoundingPoly): Float {
    // Try pixel vertices first (non-nullable list)
    val vertices = boundingPoly.vertices
    if (vertices.size >= 4) {
        // Cloud Vision order: [0]=top-left, [1]=top-right, [2]=bottom-right, [3]=bottom-left
        val bottomLeft = vertices[3]
        val bottomRight = vertices[2]

        val deltaX = (bottomRight.x - bottomLeft.x).toFloat()
        val deltaY = (bottomRight.y - bottomLeft.y).toFloat()

        if (deltaX != 0f || deltaY != 0f) {
            val angleRad = atan2(deltaY, deltaX)
            val angleDeg = angleRad * (180f / PI.toFloat())
            return normalizeAngle(angleDeg)
        }
    }

    // Fallback to normalized vertices
    boundingPoly.normalizedVertices?.let { normalizedVerts ->
        if (normalizedVerts.size >= 4) {
            val bottomLeft = normalizedVerts[3]
            val bottomRight = normalizedVerts[2]

            val deltaX = bottomRight.x - bottomLeft.x
            val deltaY = bottomRight.y - bottomLeft.y

            if (deltaX != 0f || deltaY != 0f) {
                val angleRad = atan2(deltaY, deltaX)
                val angleDeg = angleRad * (180f / PI.toFloat())
                return normalizeAngle(angleDeg)
            }
        }
    }

    return 0f
}

/**
 * Normalizes angle to -180 to 180 range
 */
private fun normalizeAngle(angleDeg: Float): Float {
    return when {
        angleDeg > 180f -> angleDeg - 360f
        angleDeg < -180f -> angleDeg + 360f
        else -> angleDeg
    }
}

/**
 * Converts Cloud Vision BoundingPoly to normalized BoundingBox (0-1 range)
 */
private fun convertBoundingPoly(
    boundingPoly: BoundingPoly,
    imageWidth: Float,
    imageHeight: Float
): BoundingBox {
    // PRIORITY 1: Use normalized vertices if available (they're already 0-1)
    boundingPoly.normalizedVertices?.let { normalizedVerts ->
        if (normalizedVerts.isNotEmpty()) {
            val xs = normalizedVerts.map { it.x }
            val ys = normalizedVerts.map { it.y }

            val left = xs.minOrNull() ?: 0f
            val top = ys.minOrNull() ?: 0f
            val right = xs.maxOrNull() ?: 1f
            val bottom = ys.maxOrNull() ?: 1f

            println("Cloud Vision Normalized Vertices: left=$left, top=$top, right=$right, bottom=$bottom")

            return BoundingBox(
                left = left.coerceIn(0f, 1f),
                top = top.coerceIn(0f, 1f),
                right = right.coerceIn(0f, 1f),
                bottom = bottom.coerceIn(0f, 1f)
            )
        }
    }

    // PRIORITY 2: Convert pixel vertices to normalized coordinates
    val vertices = boundingPoly.vertices
    if (vertices.isEmpty()) {
        return BoundingBox(0f, 0f, 1f, 1f)
    }

    val xs = vertices.map { it.x.toFloat() }
    val ys = vertices.map { it.y.toFloat() }

    val left = (xs.minOrNull() ?: 0f) / imageWidth
    val top = (ys.minOrNull() ?: 0f) / imageHeight
    val right = (xs.maxOrNull() ?: imageWidth) / imageWidth
    val bottom = (ys.maxOrNull() ?: imageHeight) / imageHeight

    println("Cloud Vision Pixel Vertices converted: left=$left, top=$top, right=$right, bottom=$bottom (imageSize: ${imageWidth}x${imageHeight})")

    return BoundingBox(
        left = left.coerceIn(0f, 1f),
        top = top.coerceIn(0f, 1f),
        right = right.coerceIn(0f, 1f),
        bottom = bottom.coerceIn(0f, 1f)
    )
}

/**
 * Maps blocks from fullTextAnnotation (preferred method)
 * Provides structured text with paragraphs and words
 */
private fun mapBlocksFromFullTextAnnotation(fullTextAnnotation: TextAnnotation): List<TextBlock> {
    val blocks = mutableListOf<TextBlock>()

    fullTextAnnotation.pages.forEach { page ->
        val imageWidth = page.width.toFloat()
        val imageHeight = page.height.toFloat()

        page.blocks.forEach { block ->
            // Extract all text from paragraphs
            val blockText = block.paragraphs.joinToString("\n") { paragraph ->
                paragraph.words.joinToString(" ") { word ->
                    word.symbols.joinToString("") { it.text }
                }
            }

            // Extract lines from paragraphs
            val lines = block.paragraphs.map { paragraph ->
                val lineText = paragraph.words.joinToString(" ") { word ->
                    word.symbols.joinToString("") { it.text }
                }
                TextLine(
                    text = lineText,
                    confidence = paragraph.confidence ?: 0f
                )
            }

            // Convert bounding box to normalized coordinates
            val boundingBox = convertBoundingPoly(
                block.boundingBox,
                imageWidth,
                imageHeight
            )

            // Calculate rotation angle from bounding box vertices
            val rotationAngle = calculateRotationAngleFromCloudVision(block.boundingBox)

            blocks.add(
                TextBlock(
                    text = blockText,
                    lines = lines,
                    boundingBox = boundingBox,
                    confidence = block.confidence ?: 0f,
                    rotationAngle = rotationAngle
                )
            )
        }
    }

    return blocks
}

/**
 * Fallback method: maps blocks from textAnnotations
 * Used when fullTextAnnotation is not available
 */
private fun mapBlocksFromTextAnnotations(textAnnotations: List<EntityAnnotation>): List<TextBlock> {
    // Skip first annotation (contains full text)
    return textAnnotations.drop(1).mapNotNull { annotation ->
        val boundingPoly = annotation.boundingPoly ?: return@mapNotNull null

        // Estimate image dimensions from vertices
        val maxX = boundingPoly.vertices.maxOfOrNull { it.x }?.toFloat() ?: 1f
        val maxY = boundingPoly.vertices.maxOfOrNull { it.y }?.toFloat() ?: 1f

        val boundingBox = convertBoundingPoly(boundingPoly, maxX, maxY)
        val rotationAngle = calculateRotationAngleFromCloudVision(boundingPoly)

        TextBlock(
            text = annotation.description,
            lines = listOf(
                TextLine(
                    text = annotation.description,
                    confidence = annotation.confidence ?: annotation.score ?: 0f
                )
            ),
            boundingBox = boundingBox,
            confidence = annotation.confidence ?: annotation.score ?: 0f,
            rotationAngle = rotationAngle
        )
    }
}

/**
 * Maps Cloud Vision API response to OCRResult format
 * Uses fullTextAnnotation for structured text with blocks
 */
fun mapToOCRResult(response: AnnotateImageResponse): OCRResult {
    val fullTextAnnotation = response.fullTextAnnotation
    val textAnnotations = response.textAnnotations

    // Get full text (first annotation contains all text)
    val fullText = textAnnotations?.firstOrNull()?.description ?: ""

    // Get detected language from first annotation
    val detectedLanguage = textAnnotations?.firstOrNull()?.locale

    // Map blocks from fullTextAnnotation
    val blocks = if (fullTextAnnotation != null) {
        mapBlocksFromFullTextAnnotation(fullTextAnnotation)
    } else {
        // Fallback: create blocks from textAnnotations
        mapBlocksFromTextAnnotations(textAnnotations ?: emptyList())
    }

    // Calculate overall confidence
    val overallConfidence = if (blocks.isNotEmpty()) {
        blocks.map { it.confidence }.average().toFloat()
    } else {
        0f
    }

    return OCRResult(
        fullText = fullText,
        blocks = blocks,
        overallConfidence = overallConfidence,
        detectedLanguage = detectedLanguage,
        engine = OCREngine.GOOGLE_CLOUD_VISION
    )
}