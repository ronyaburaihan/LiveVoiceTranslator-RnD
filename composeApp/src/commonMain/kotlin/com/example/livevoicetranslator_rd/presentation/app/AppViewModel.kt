package com.example.livevoicetranslator_rd.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    private val _appState = MutableStateFlow<AppState?>(null)
    val appState = _appState.asStateFlow()


    init {
        makeUserPremium()
    }

    private fun makeUserPremium() {
        viewModelScope.launch {
            delay(0)
            _appState.value = AppState.Lifetime
        }
    }
}