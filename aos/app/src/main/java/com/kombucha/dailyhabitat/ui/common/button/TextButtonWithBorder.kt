package com.kombucha.dailyhabitat.ui.common.button

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextButtonWithBorder(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    text: String,
    onClick : () -> Unit,
) {
    Text(
        text = text,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(contentPadding)
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .then(modifier)
            .clickable { onClick.invoke() }

    )
}