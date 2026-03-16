package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Req(val title: String, val time: String, val status: String)

@Composable
fun RequestHistoryScreen() {
    val data = listOf(
        Req("Towels", "Today • 10:40 AM", "Pending"),
        Req("Room Cleaning", "Yesterday • 6:20 PM", "Completed"),
        Req("Maintenance", "Yesterday • 4:10 PM", "In Progress"),
    )

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Redundant title removed as it is now in TopAppBar

        Text("Track your housekeeping requests.", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(data) { r ->
                Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(1.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(r.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(4.dp))
                            Text(r.time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))
                        }
                        StatusChip(status = r.status)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status.lowercase()) {
        "pending" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f)
        "in progress" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        "completed" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(color = color, shape = MaterialTheme.shapes.large) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}