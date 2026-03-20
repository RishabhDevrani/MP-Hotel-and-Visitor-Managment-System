package com.example.guestservicesapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guestservicesapp.viewmodel.SharedViewModel
import kotlinx.coroutines.delay

private data class HousekeepingType(val title: String, val subtitle: String, val color: Color)

@Composable
fun HousekeepingScreen(
    sharedVm: SharedViewModel,
    onOpenHistory: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val services = listOf(
        HousekeepingType("Room Cleaning", "Standard room cleaning", Color(0xFF6C63FF)),
        HousekeepingType("Towels", "Fresh towels request", Color(0xFFFF4D6D)),
        HousekeepingType("Water", "Extra water bottles", Color(0xFF4CAF50)),
        HousekeepingType("Laundry", "Laundry pickup request", Color(0xFFFFA62B)),
        HousekeepingType("Maintenance", "Report an issue", Color(0xFF2EC4B6))
    )

    var selected by remember { mutableStateOf<HousekeepingType?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var lastRequested by remember { mutableStateOf("") }

    val gradient = if (isDark) {
        Brush.verticalGradient(colors = listOf(Color(0xFF1A1C22), Color(0xFF0F1115)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF1F3F9), Color.White))
    }

    Box(Modifier.fillMaxSize().background(gradient)) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onOpenHistory) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("View History", fontWeight = FontWeight.Bold)
                }
            }

            Text(
                "How can we help you today?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(services) { s ->
                    ServiceItemCard(s) {
                        selected = s
                    }
                }
            }
        }

        if (selected != null) {
            CreateRequestBottomSheet(
                serviceTitle = selected!!.title,
                onDismiss = { selected = null },
                onSubmit = { note ->
                    sharedVm.addRequest(selected!!.title, note)
                    lastRequested = selected!!.title
                    selected = null
                    showSuccess = true
                }
            )
        }

        AnimatedVisibility(
            visible = showSuccess,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        ) {
            Surface(
                color = Color(0xFF4CAF50),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 8.dp
            ) {
                Row(Modifier.padding(horizontal = 24.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Request for $lastRequested sent!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            LaunchedEffect(showSuccess) {
                if (showSuccess) {
                    delay(3000)
                    showSuccess = false
                }
            }
        }
    }
}

@Composable
private fun ServiceItemCard(service: HousekeepingType, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    
    val cardGradient = Brush.linearGradient(
        colors = listOf(
            service.color.copy(alpha = if (isDark) 0.15f else 0.12f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(service.color.copy(alpha = 0.3f), Color.Transparent)),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            Modifier
                .background(cardGradient)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(18.dp),
                color = service.color
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        service.title.first().toString(), 
                        fontWeight = FontWeight.Black, 
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    service.title, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    service.subtitle, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
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
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                "New Request", 
                style = MaterialTheme.typography.headlineSmall, 
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "You are requesting: $serviceTitle", 
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Special instructions (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSubmit(note) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirm Request", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
