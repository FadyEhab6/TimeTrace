package com.example.timetrace2.ui.report

import androidx.compose.ui.graphics.Color

internal enum class ReportPeriod(val label: String) {
    Daily("Daily"),
    Weekly("Weekly"),
    Monthly("Monthly"),
    Custom("Custom"),
}

internal data class ReportCategory(
    val name: String,
    val percent: Int,
    val duration: String,
    val color: Color,
)

internal data class ReportData(
    val totalTime: String = "0m",
    val categories: List<ReportCategory> = emptyList(),
) {
    val hasData: Boolean
        get() = categories.isNotEmpty()
}
