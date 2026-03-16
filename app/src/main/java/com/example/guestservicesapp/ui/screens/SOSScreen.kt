package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SOSScreen() {
    val options = listOf(
        "Medical Help" to "Request medical assistance",
        "Emergency" to "Urgent help required",
        "Fire" to "Report fire or smoke",
        "General Assistance" to "Immediate support needed"
    )

    var lastSent by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Redundant SOS title removed as it is now in TopAppBar

        Text(
            "Tap to send an SOS request. (Will be logged to hotel system)",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f)
        )

        options.forEach { (title, sub) ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable {
                    // Later: call backend to log SOS
                    lastSent = title
                },
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))
                }
            }
        }

        if (lastSent != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                "✅ SOS sent: $lastSent",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}