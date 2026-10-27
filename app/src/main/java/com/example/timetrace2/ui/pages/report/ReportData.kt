package com.example.timetrace2.ui.report

import androidx.compose.ui.graphics.Color
import com.example.timetrace2.ui.theme.HealthGreen
import com.example.timetrace2.ui.theme.LeisurePurple
import com.example.timetrace2.ui.theme.StudyBlue
import com.example.timetrace2.ui.theme.WorkOrange

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
    val totalTime: String,
    val categories: List<ReportCategory>,
)

internal object ReportDataProvider {
    fun forPeriod(period: ReportPeriod): ReportData = when (period) {
        ReportPeriod.Daily -> ReportData(
            totalTime = "4h 30m",
            categories = listOf(
                ReportCategory("Study", 50, "2h 15m", StudyBlue),
                ReportCategory("Work", 30, "1h 20m", WorkOrange),
                ReportCategory("Health", 15, "40m", HealthGreen),
                ReportCategory("Leisure", 5, "15m", LeisurePurple),
            ),
        )
        ReportPeriod.Weekly -> ReportData(
            totalTime = "31h 45m",
            categories = listOf(
                ReportCategory("Study", 45, "14h 20m", StudyBlue),
                ReportCategory("Work", 35, "11h 05m", WorkOrange),
                ReportCategory("Health", 12, "3h 50m", HealthGreen),
                ReportCategory("Leisure", 8, "2h 30m", LeisurePurple),
            ),
        )
        ReportPeriod.Monthly -> ReportData(
            totalTime = "126h 10m",
            categories = listOf(
                ReportCategory("Study", 42, "52h 55m", StudyBlue),
                ReportCategory("Work", 38, "47h 55m", WorkOrange),
                ReportCategory("Health", 13, "16h 25m", HealthGreen),
                ReportCategory("Leisure", 7, "8h 55m", LeisurePurple),
            ),
        )
        ReportPeriod.Custom -> ReportData(
            totalTime = "18h 40m",
            categories = listOf(
                ReportCategory("Study", 55, "10h 15m", StudyBlue),
                ReportCategory("Work", 25, "4h 40m", WorkOrange),
                ReportCategory("Health", 12, "2h 15m", HealthGreen),
                ReportCategory("Leisure", 8, "1h 30m", LeisurePurple),
            ),
        )
    }
}
