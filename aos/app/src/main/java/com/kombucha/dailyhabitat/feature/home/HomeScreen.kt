package com.kombucha.dailyhabitat.feature.home

import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUiScreen(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(

    ) { innerPadding ->
        innerPadding
    }
}
