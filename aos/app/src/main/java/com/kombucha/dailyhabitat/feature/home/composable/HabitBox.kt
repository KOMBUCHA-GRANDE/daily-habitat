package com.kombucha.dailyhabitat.feature.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kombucha.dailyhabitat.ui.common.button.TextButtonWithBorder

@Composable
fun HabitBox(
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "매일 운동하기",
                    fontSize = 18.sp,
                    color = Color.Black,
                )
                Text(
                    text = "23일 째",
                    fontSize = 12.sp,
                    color = Color.Black,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "시작일 : 2024.02.01",
                fontSize = 11.sp,
                color = Color.LightGray,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "연속 달성 : 23일",
                fontSize = 11.sp,
                color = Color.LightGray,
            )
        }
        LinearProgressIndicator(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp)),
            trackColor = Color.LightGray,
            color = Color.Black,
        )
        TextButtonWithBorder(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp),
            text = "오늘의 일기 작성하기",
            onClick = {},
        )
    }
}