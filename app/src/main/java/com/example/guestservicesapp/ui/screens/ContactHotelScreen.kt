package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ContactHotelScreen() {
    val contacts = listOf(
        "Reception" to "+91 98XXXXXX01",
        "Room Service" to "+91 98XXXXXX02",
        "Housekeeping Desk" to "+91 98XXXXXX03",
        "Emergency Desk" to "+91 98XXXXXX04"
    )

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Redundant title removed as it is now in TopAppBar

        Text("Contact information (display only).", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))

        contacts.forEach { (dept, phone) ->
            Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(1.dp)) {
                Column(Modifier.padding(14.dp)) {
                    Text(dept, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(phone, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}