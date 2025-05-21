package com.kombucha.dailyhabitat.feature.home

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kombucha.dailyhabitat.ui.common.button.TextButtonWithLeftButton
import com.kombucha.dailyhabitat.ui.common.topbar.TopBar
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
        modifier = modifier,
        topBar = {
            TopBar(
                leftAction = { modifier ->
                    Text(
                        text = "Logo",
                        fontSize = 18.sp,
                        modifier = modifier,
                    )
                },
                rightAction = { modifier ->
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = modifier
                            .size(24.dp)
                            .clickable {  },
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .padding(top = 8.dp)
                .fillMaxSize()
        ) {
            TextButtonWithLeftButton(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp),
                image = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = "새로운 습관 만들기",
                onClick = {},
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {

            }
        }

    }
}
