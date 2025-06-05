package com.kombucha.dailyhabitat.presentation.feature.home

import androidx.compose.runtime.Composable
import com.kombucha.dailyhabitat.domain.repository.HabitatRepository
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val habitatRepository: HabitatRepository,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        return HomeUiState(
            habitatList = emptyList()
        ) { event ->
            when(event)
            {
                HomeEvent.EntranceScreen -> {
                    habitatRepository.getHabitatList()
                }
                else -> {}
            }
        }
    }

    @CircuitInject(Screen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface  Factory {
        fun create(
            navigator: Navigator,
        ): HomePresenter
    }

}