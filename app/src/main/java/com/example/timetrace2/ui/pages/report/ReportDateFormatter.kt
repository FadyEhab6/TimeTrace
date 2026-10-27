package com.example.timetrace2.ui.report

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object ReportDateFormatter {
    private val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US)

    fun format(dateMillis: Long): String = formatter.format(Date(dateMillis))
}
