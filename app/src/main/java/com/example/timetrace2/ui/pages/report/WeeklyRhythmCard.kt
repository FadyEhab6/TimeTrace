package com.example.timetrace2.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.data.DaySummary
import com.example.timetrace2.ui.components.SectionCard
import com.example.timetrace2.ui.components.SectionLabel
import com.example.timetrace2.ui.theme.DividerGray
import com.example.timetrace2.ui.theme.TextMuted

private val ChartHeight = 210.dp
private const val MaxHours = 20f
private val YAxisLabels = listOf("20h", "15h", "10h", "5h", "0h")

@Composable
fun WeeklyRhythmCard(
    week: List<DaySummary>,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier) {
        SectionLabel("WEEKLY RHYTHM")
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            YAxis()
            Column(Modifier.weight(1f)) {
                RhythmChart(week)
                Spacer(Modifier.height(8.dp))
                DayLabels(week)
            }
        }
    }
}

@Composable
private fun YAxis() {
    Column(
        Modifier.height(ChartHeight).width(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        YAxisLabels.forEach { Text(it, color = TextMuted, fontSize = 14.sp) }
    }
}

@Composable
private fun RhythmChart(week: List<DaySummary>) {
    Canvas(Modifier.fillMaxWidth().height(ChartHeight)) {
        // Horizontal grid lines
        for (i in 0..4) {
            val y = size.height * i / 4f
            drawLine(DividerGray, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
        }

        // One stacked bar per day
        val slotWidth = size.width / week.size
        val barWidth = slotWidth * 0.7f

        week.forEachIndexed { index, day ->
            val x = index * slotWidth + (slotWidth - barWidth) / 2f
            var bottom = size.height

            day.segments.forEachIndexed { i, segment ->
                val height = segment.hours / MaxHours * size.height
                val isTopSegment = i == day.segments.lastIndex
                drawRoundRect(
                    color = segment.color,
                    topLeft = Offset(x, bottom - height),
                    size = Size(barWidth, height),
                    cornerRadius = if (isTopSegment) CornerRadius(4.dp.toPx()) else CornerRadius.Zero,
                )
                bottom -= height
            }
        }
    }
}

@Composable
private fun DayLabels(week: List<DaySummary>) {
    Row(Modifier.fillMaxWidth()) {
        week.forEach { day ->
            Text(
                day.day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = TextMuted,
                fontSize = 15.sp,
            )
        }
    }
}
