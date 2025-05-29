package com.kombucha.dailyhabitat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.kombucha.dailyhabitat.di.DailyHabitatModule
import com.kombucha.dailyhabitat.presentation.feature.home.HomeScreen
import com.kombucha.dailyhabitat.presentation.feature.home.HomeUiScreen
import com.kombucha.dailyhabitat.presentation.feature.home.HomeUiState
import com.kombucha.dailyhabitat.presentation.ui.theme.DailyHabitatTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberSaveableBackStack(root = HomeScreen)
            val navigator = rememberCircuitNavigator(backStack)
            DailyHabitatTheme {
                CircuitCompositionLocals(circuit) {
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = Modifier.systemBarsPadding()
                    )
                }

            }
        }
    }
}
