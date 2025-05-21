package com.kombucha.dailyhabitat.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextButtonWithLeftButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    image: @Composable () -> Unit,
    text: String,
    textColor: Color = Color.White,
) {
    Row(
        modifier = Modifier
            .padding(contentPadding)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        image.invoke()
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            color = textColor,
        )
    }
}