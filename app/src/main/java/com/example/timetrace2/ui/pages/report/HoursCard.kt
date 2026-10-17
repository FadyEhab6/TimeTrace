package com.example.timetrace2.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.data.BarPart
import com.example.timetrace2.ui.components.SectionCard
import com.example.timetrace2.ui.components.SectionLabel
import com.example.timetrace2.ui.theme.SuccessGreen
import com.example.timetrace2.ui.theme.TextMuted

@Composable
fun HoursCard(
    parts: List<BarPart>,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SectionLabel("168 HOURS")
            Text("7 of 7 days", color = TextMuted, fontSize = 14.sp)
        }
        Spacer(Modifier.height(18.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            parts.forEach { part ->
                Box(Modifier.weight(part.weight).fillMaxHeight().background(part.color))
            }
        }
        Spacer(Modifier.height(18.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                "99% accounted for",
                modifier = Modifier.weight(1f),
                color = SuccessGreen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
            )
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = TextMuted)
        }
    }
}
