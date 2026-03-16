package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class HousekeepingType(val title: String, val subtitle: String)

@Composable
fun HousekeepingScreen(onOpenHistory: () -> Unit) {
    val services = listOf(
        HousekeepingType("Room Cleaning", "Standard room cleaning"),
        HousekeepingType("Towels", "Fresh towels request"),
        HousekeepingType("Water", "Extra water bottles"),
        HousekeepingType("Laundry", "Laundry pickup request"),
        HousekeepingType("Maintenance", "Report an issue")
    )

    var selected by remember { mutableStateOf<HousekeepingType?>(null) }

    // Bottom sheet state
    if (selected != null) {
        CreateRequestBottomSheet(
            serviceTitle = selected!!.title,
            onDismiss = { selected = null },
            onSubmit = { note ->
                // Later: call backend API to create request
                selected = null
            }
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onOpenHistory) { Text("History") }
        }

        Text(
            "Choose a service and submit a request.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(services) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selected = s },
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(s.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(s.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateRequestBottomSheet(
    serviceTitle: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var note by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("New Request", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(serviceTitle, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f))
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onSubmit(note) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Submit Request")
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}