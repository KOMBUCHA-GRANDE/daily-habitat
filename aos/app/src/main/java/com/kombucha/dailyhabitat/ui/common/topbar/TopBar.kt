package com.kombucha.dailyhabitat.ui.common.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    leftAction: (@Composable (Modifier) -> Unit)? = null,
    onNavigationButtonClick: (() -> Unit)? = null,
    title: String? = null,
    rightAction: (@Composable (Modifier) -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        onNavigationButtonClick?.let { navigationButton ->
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { navigationButton.invoke() }
            )
        }

        leftAction?.invoke(
            Modifier.align(Alignment.CenterStart)
        );

        title?.let { title ->
            Text(
                text = title,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        rightAction?.invoke(
             Modifier.align(Alignment.CenterEnd)
        );
    }
}