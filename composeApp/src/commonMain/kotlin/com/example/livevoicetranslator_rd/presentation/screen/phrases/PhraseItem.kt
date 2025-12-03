package com.example.livevoicetranslator_rd.presentation.screen.phrases

data class PhraseItem(
    val id: Int,
    val sourceText: String,
    val targetText: String,
    var isExpanded: Boolean = false,
    var isFavorite: Boolean = false
)
