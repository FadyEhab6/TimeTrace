package com.example.timetrace2.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.timetrace2.ui.theme.TextMuted

/** Small spaced-out grey heading such as "TOP ZONES". */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = TextMuted,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 2.sp,
    )
}
