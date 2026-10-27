package com.example.timetrace2.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.ui.theme.*



private val DefaultReportDateMillis = java.time.LocalDate.of(2025, 4, 26)
    .atStartOfDay(java.time.ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()

@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    var selectedPeriod by rememberSaveable { mutableStateOf(ReportPeriod.Daily) }
    var selectedDateMillis by rememberSaveable { mutableStateOf(DefaultReportDateMillis) }
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }
    val reportData = ReportDataProvider.forPeriod(selectedPeriod)

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(ScreenBackground)
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState()),
        ) {
        Text(
            text = "Reports",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            color = ReportNavy,
            fontSize = Typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
        )
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { selectedPeriod = it },
        )
        DateSelector(
            selectedDate = ReportDateFormatter.format(selectedDateMillis),
            onClick = { isDatePickerVisible = true },
        )

        Text(
            text = "Total time",
            modifier = Modifier.padding(top = 10.dp),
            color = ReportBlue,
            fontSize = 14.sp,
        )
        Text(
            text = reportData.totalTime,
            color = ReportNavy,
            fontSize = Typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold,
        )
        TotalTimeBreakdown(reportData)
        Text(
            text = "Time by category",
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
            color = ReportNavy,
            fontSize = Typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold,
        )
        CategoryBreakdown(reportData.categories)
    }

    if (isDatePickerVisible) {
        ReportDatePickerDialog(
            initialDateMillis = selectedDateMillis,
            onDateSelected = { selectedDateMillis = it },
            onDismiss = { isDatePickerVisible = false },
        )
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: ReportPeriod,
    onPeriodSelected: (ReportPeriod) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DividerGray, RoundedCornerShape(8.dp)),
    ) {
        ReportPeriod.entries.forEach { period ->
            Text(
                text = period.label,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (period == selectedPeriod) StudyBlue else Color.Transparent)
                    .clickable { onPeriodSelected(period) }
                    .padding(vertical = 5.dp),
                color = if (period == selectedPeriod) Color.White else ReportBlue,
                fontSize = Typography.labelLarge.fontSize,
                fontWeight = if (period == selectedPeriod) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DividerGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Select date",
            tint = ReportBlue,
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = selectedDate,
            modifier = Modifier.weight(1f),
            color = ReportNavy,
            fontSize = Typography.titleSmall.fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Next date",
            tint = ReportBlue,
            modifier = Modifier.size(12.dp),
        )
    }
}

@Composable
private fun TotalTimeBreakdown(reportData: ReportData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            DonutChart(reportData)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            reportData.categories.forEach { category ->
                LegendRow(category)
            }
        }
    }
}

@Composable
private fun DonutChart(reportData: ReportData) {
    Box(
        modifier = Modifier.size(112.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 17.dp.toPx()
            var startAngle = -90f
            reportData.categories.forEach { category ->
                val sweep = category.percent * 3.6f
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                )
                startAngle += sweep
            }
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2f - strokeWidth,
            )
        }
        Text(
            text = reportData.totalTime,
            color = ReportNavy,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}


@Composable
private fun LegendRow(category: ReportCategory) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Category column: takes the remaining space
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(category.color)
            )

            Text(
                text = category.name,
                modifier = Modifier.padding(start = 8.dp),
                color = TextMuted,
                fontSize = 11.sp,
            )
        }
        Text(
            text = "${category.percent}%",
            modifier = Modifier.width(50.dp),
            color = TextMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = category.duration,
            modifier = Modifier.width(60.dp),
            color = TextMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
        )
    }
}




@Composable
private fun CategoryBreakdown(categories: List<ReportCategory>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        categories.forEach { category ->
            Column(
                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Category title above the progress bar
                Text(
                    text = category.name,
                    color = ReportNavy,
                    fontWeight = FontWeight.W500,
                    fontSize = Typography.bodySmall.fontSize,
                    style = Typography.bodySmall.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )

                // Progress bar and duration on the same row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Fixed-width track area shared by every category
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(9.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFE7ECF2)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(category.percent.coerceIn(0, 100) / 100f)
                                .clip(RoundedCornerShape(50))
                                .background(category.color),
                        )
                    }

                    // Fixed width keeps all progress tracks aligned
                    Text(
                        text = category.duration,
                        modifier = Modifier.width(42.dp),
                        color = ReportNavy,
                        fontSize = Typography.bodySmall.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        style = Typography.bodySmall.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
        }
    }
}





@Preview(showBackground = true, widthDp = 400, heightDp = 860)
@Composable
private fun ReportScreenPreview() {
    TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
        ReportScreen()
    }
}
