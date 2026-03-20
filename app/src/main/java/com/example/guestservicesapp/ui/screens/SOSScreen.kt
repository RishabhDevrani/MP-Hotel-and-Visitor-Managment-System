package com.example.guestservicesapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private data class SOSOption(val title: String, val sub: String, val color: Color)

@Composable
fun SOSScreen() {
    val options = listOf(
        SOSOption("Medical Help", "Request medical assistance", Color(0xFFFFEBEE)),
        SOSOption("Emergency", "Urgent help required", Color(0xFFF3E5F5)),
        SOSOption("Fire", "Report fire or smoke", Color(0xFFFFF3E0)),
        SOSOption("General Help", "Immediate support needed", Color(0xFFE3F2FD))
    )

    var lastSent by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    val gradient = if (isDark) {
        Brush.verticalGradient(colors = listOf(Color(0xFF2C1010), Color(0xFF0F1115)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFFFEBEE), Color.White))
    }

    Box(Modifier.fillMaxSize().background(gradient)) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(
                    "Emergency SOS",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isDark) Color(0xFFFF8A80) else Color(0xFFD32F2F)
                )
                Text(
                    "Tap an option below for immediate assistance.",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(options) { opt ->
                    SOSCard(opt) {
                        lastSent = opt.title
                        showSuccess = true
                    }
                }
            }
        }

        // Success Popup
        AnimatedVisibility(
            visible = showSuccess,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        ) {
            Surface(
                color = if (isDark) Color(0xFFFF5252) else Color(0xFFD32F2F),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 8.dp
            ) {
                Row(Modifier.padding(horizontal = 24.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text("$lastSent request logged!", color = Color.White, fontWeight = FontWeight.Bold)
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
private fun SOSCard(option: SOSOption, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(20.dp),
                color = if (isDark) option.color.copy(alpha = 0.2f) else option.color
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.NotificationsActive, 
                        contentDescription = null, 
                        tint = if (isDark) Color(0xFFFF8A80) else Color(0xFFD32F2F)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    option.title, 
                    fontWeight = FontWeight.Black, 
                    fontSize = 18.sp, 
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    option.sub, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
