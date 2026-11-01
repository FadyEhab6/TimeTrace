package com.example.timetrace2.ui.pages.report

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.timetrace2.ui.report.ReportCategory
import com.example.timetrace2.ui.report.ReportData
import com.example.timetrace2.ui.report.ReportPeriod
import com.example.timetrace2.ui.report.ReportViewModel
import com.example.timetrace2.ui.theme.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// 7awel el milliseconds l string "Nov 1, 2026" 3shan yetzabat fel date selector
private fun formatDate(dateMillis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
    return Instant.ofEpochMilli(dateMillis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

// el screen el ra2esiya bta3et el reports
@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    val reportViewModel: ReportViewModel = viewModel()
    val selectedPeriod by reportViewModel.selectedPeriod.collectAsState()
    val selectedDateMillis by reportViewModel.selectedDateMillis.collectAsState()
    val reportData by reportViewModel.report.collectAsState()

    // hena bnsaytrol el date picker yetzabat wala la
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(ScreenBackground)
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // 3onwan el screen
        Text(
            text = "Reports",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            color = ReportNavy,
            fontSize = Typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
        )

        // selector el period: daily / weekly / monthly / custom
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = reportViewModel::selectPeriod,
        )

        // selector el date — lma t3oso yftah el calendar
        DateSelector(
            selectedDate = formatDate(selectedDateMillis),
            onClick = { isDatePickerVisible = true },
        )

        // lo fi data nwreiha, lo la nwri message fadya
        if (reportData.hasData) {
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
        } else {
            NoReportDataMessage()
        }
    }

    // el calendar popup — bytzhr bs lma el user y3os 3al date
    if (isDatePickerVisible) {
        ReportDatePickerDialog(
            initialDateMillis = selectedDateMillis,
            onDateSelected = reportViewModel::selectDate,
            onDismiss = { isDatePickerVisible = false },
        )
    }
}

// message lma mafi data fel period de
@Composable
private fun NoReportDataMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, bottom = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No Data to show",
            color = TextMuted,
            fontSize = Typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
        )
    }
}

// calendar popup — el user bykhtar meno el date
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportDatePickerDialog(
    initialDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// tabs: daily / weekly / monthly / custom
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

// el row elly byewri el date el mokhtar — lma t3os 3aleh byftah el calendar
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
        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Select date", tint = ReportBlue, modifier = Modifier.size(15.dp))
        Text(text = selectedDate, modifier = Modifier.weight(1f), color = ReportNavy, fontSize = Typography.titleSmall.fontSize, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Next date", tint = ReportBlue, modifier = Modifier.size(12.dp))
    }
}

// el donut chart wel legend ganboh
@Composable
private fun TotalTimeBreakdown(reportData: ReportData) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            DonutChart(reportData)
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            reportData.categories.forEach { category -> LegendRow(category) }
        }
    }
}

// el chart el da2ri elly byewri el categories bel colors
@Composable
private fun DonutChart(reportData: ReportData) {
    Box(modifier = Modifier.size(112.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 17.dp.toPx()
            var startAngle = -90f
            reportData.categories.forEach { category ->
                val sweep = category.percent * 3.6f
                drawArc(color = category.color, startAngle = startAngle, sweepAngle = sweep, useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Butt))
                startAngle += sweep
            }
            drawCircle(color = Color.White, radius = size.minDimension / 2f - strokeWidth)
        }
        Text(text = reportData.totalTime, color = ReportNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

// row wahda fel legend: el color + el esm + el % + el mudda
@Composable
private fun LegendRow(category: ReportCategory) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(50)).background(category.color))
            Text(text = category.name, modifier = Modifier.padding(start = 8.dp), color = TextMuted, fontSize = 11.sp)
        }
        Text(text = "${category.percent}%", modifier = Modifier.width(50.dp), color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
        Text(text = category.duration, modifier = Modifier.width(60.dp), color = TextMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

// lista bel categories — kol wa7da fiha progress bar wel mudda
@Composable
private fun CategoryBreakdown(categories: List<ReportCategory>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        categories.forEach { category ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = category.name,
                    color = ReportNavy,
                    fontWeight = FontWeight.W500,
                    fontSize = Typography.bodySmall.fontSize,
                    style = Typography.bodySmall.copy(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // el progress bar — el tol byt3adal 3ala 7asab el %
                    Box(modifier = Modifier.weight(1f).height(9.dp).clip(RoundedCornerShape(50)).background(Color(0xFFE7ECF2))) {
                        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(category.percent.coerceIn(0, 100) / 100f).clip(RoundedCornerShape(50)).background(category.color))
                    }
                    Text(
                        text = category.duration,
                        modifier = Modifier.width(42.dp),
                        color = ReportNavy,
                        fontSize = Typography.bodySmall.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        style = Typography.bodySmall.copy(platformStyle = PlatformTextStyle(includeFontPadding = false))
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
