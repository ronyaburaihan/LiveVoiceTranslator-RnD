package com.example.livevoicetranslator_rd.domain.model.speachtotext

sealed class SpeachResult<out T> {
    data class Success<out T>(val data: T) : SpeachResult<T>()
    data class Error(val exception: Exception) : SpeachResult<Nothing>()
    object Loading : SpeachResult<Nothing>()
}