package com.example.livevoicetranslator_rd.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VisionApiResponse(
    val responses: List<AnnotateImageResponse>? = null
)

@Serializable
data class AnnotateImageResponse(
    val textAnnotations: List<EntityAnnotation>? = null,
    val fullTextAnnotation: TextAnnotation? = null,
    val error: ErrorStatus? = null
)

@Serializable
data class EntityAnnotation(
    val locale: String? = null,
    val description: String,
    val score: Float? = null,
    val confidence: Float? = null,
    val boundingPoly: BoundingPoly? = null
)

@Serializable
data class TextAnnotation(
    val pages: List<Page> = emptyList(),
    val text: String
)

@Serializable
data class Page(
    val property: TextProperty? = null,
    val width: Int,
    val height: Int,
    val blocks: List<Block>,
    val confidence: Float? = null
)

@Serializable
data class Block(
    val property: TextProperty? = null,
    val boundingBox: BoundingPoly,
    val paragraphs: List<Paragraph>,
    val blockType: String? = null,
    val confidence: Float? = null
)

@Serializable
data class Paragraph(
    val property: TextProperty? = null,
    val boundingBox: BoundingPoly,
    val words: List<Word>,
    val confidence: Float? = null
)

@Serializable
data class Word(
    val property: TextProperty? = null,
    val boundingBox: BoundingPoly,
    val symbols: List<Symbol>,
    val confidence: Float? = null
)

@Serializable
data class Symbol(
    val property: TextProperty? = null,
    val boundingBox: BoundingPoly,
    val text: String,
    val confidence: Float? = null
)

@Serializable
data class TextProperty(
    val detectedLanguages: List<DetectedLanguage>? = null,
    val detectedBreak: DetectedBreak? = null
)

@Serializable
data class DetectedLanguage(
    val languageCode: String,
    val confidence: Float? = null
)

@Serializable
data class DetectedBreak(
    val type: String,
    val isPrefix: Boolean? = null
)

@Serializable
data class BoundingPoly(
    val vertices: List<Vertex>,
    val normalizedVertices: List<NormalizedVertex>? = null
)

@Serializable
data class Vertex(
    val x: Int = 0,
    val y: Int = 0
)

@Serializable
data class NormalizedVertex(
    val x: Float = 0f,
    val y: Float = 0f
)

@Serializable
data class ErrorStatus(
    val code: Int,
    val message: String,
    val details: List<Map<String, String>>? = null
)