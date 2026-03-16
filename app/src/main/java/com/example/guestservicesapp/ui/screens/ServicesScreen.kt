package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ServicesScreen(
    onGoHousekeeping: () -> Unit,
    onGoSOS: () -> Unit,
    onGoContact: () -> Unit,
    onGoRequestHistory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Removed redundant "Services" title as it's now in the TopAppBar

        ServiceCard(
            title = "Housekeeping",
            subtitle = "Cleaning, towels, water, maintenance",
            icon = { Icon(Icons.Filled.RoomService, contentDescription = null) },
            onClick = onGoHousekeeping
        )

        ServiceCard(
            title = "SOS",
            subtitle = "Emergency help request (logged)",
            icon = { Icon(Icons.Filled.NotificationsActive, contentDescription = null) },
            onClick = onGoSOS
        )

        ServiceCard(
            title = "Request History",
            subtitle = "Track your service requests status",
            icon = { Icon(Icons.Filled.History, contentDescription = null) },
            onClick = onGoRequestHistory
        )

        ServiceCard(
            title = "Contact Hotel",
            subtitle = "View reception & hotel contacts (card only)",
            icon = { Icon(Icons.Filled.Info, contentDescription = null) },
            onClick = onGoContact
        )
    }
}

@Composable
private fun ServiceCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)) {
                Box(Modifier.padding(10.dp)) { icon() }
            }
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))
            }
        }
    }
}