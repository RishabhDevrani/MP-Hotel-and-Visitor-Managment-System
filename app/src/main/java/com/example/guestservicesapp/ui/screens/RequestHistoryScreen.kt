package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guestservicesapp.viewmodel.SharedViewModel

@Composable
fun RequestHistoryScreen(sharedVm: SharedViewModel) {
    val requests = sharedVm.requests
    val isDark = isSystemInDarkTheme()

    val gradient = if (isDark) {
        Brush.verticalGradient(colors = listOf(Color(0xFF1A1C22), Color(0xFF0F1115)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF1F3F9), Color.White))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        if (requests.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No requests found", 
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(requests) { req ->
                    RequestHistoryCard(req.title, req.time, req.estimatedTime, req.status)
                }
            }
        }
    }
}

@Composable
private fun RequestHistoryCard(title: String, time: String, estimated: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        title, 
                        fontWeight = FontWeight.Black, 
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Requested at $time", 
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                        fontSize = 12.sp
                    )
                }
                StatusBadge(status)
            }
            
            Spacer(Modifier.height(16.dp))
            
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            
            Spacer(Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime, 
                    contentDescription = null, 
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Estimated completion: ", 
                    fontSize = 13.sp, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    estimated, 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (color, icon) = when (status.lowercase()) {
        "pending" -> Color(0xFFFFA62B) to Icons.Default.PendingActions
        "completed" -> Color(0xFF2EC4B6) to Icons.Default.CheckCircle
        else -> Color(0xFF6C63FF) to Icons.Default.AccessTime
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = color)
            Spacer(Modifier.width(4.dp))
            Text(status, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}
