package com.kombucha.dailyhabitat.presentation.feature.home

import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val eventSink: (HomeEvent) -> Unit
) : CircuitUiState
