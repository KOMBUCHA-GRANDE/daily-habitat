package com.kombucha.dailyhabitat.presentation.feature.home

import com.kombucha.dailyhabitat.domain.model.Habitat
import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val habitatList: List<Habitat>,
    val eventSink: (HomeEvent) -> Unit
) : CircuitUiState
