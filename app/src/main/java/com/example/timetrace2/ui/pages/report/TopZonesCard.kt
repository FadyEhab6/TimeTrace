package com.example.timetrace2.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.data.Zone
import com.example.timetrace2.ui.components.SectionCard
import com.example.timetrace2.ui.components.SectionLabel
import com.example.timetrace2.ui.theme.DividerGray

@Composable
fun TopZonesCard(
    zones: List<Zone>,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier) {
        SectionLabel("TOP ZONES")
        Spacer(Modifier.height(12.dp))

        zones.forEachIndexed { index, zone ->
            ZoneRow(zone)
            if (index != zones.lastIndex) HorizontalDivider(color = DividerGray)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ZoneRow(zone: Zone) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(zone.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(zone.icon, contentDescription = null, tint = zone.color, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(zone.name, fontSize = 20.sp, modifier = Modifier.weight(1f))
        Text(zone.hours, color = zone.color, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    }
}
