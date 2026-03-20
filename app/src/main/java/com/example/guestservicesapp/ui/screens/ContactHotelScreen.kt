package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactHotelScreen() {
    val contacts = listOf(
        "Reception" to "+91 98XXXXXX01",
        "Room Service" to "+91 98XXXXXX02",
        "Housekeeping Desk" to "+91 98XXXXXX03",
        "Emergency Desk" to "+91 98XXXXXX04"
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F2F1), Color.White)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(Modifier.padding(vertical = 8.dp)) {
            Text(
                "Contact Hotel",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color(0xFF00796B)
            )
            Text(
                "Direct lines for all hotel services.",
                color = Color.Black.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(contacts) { (dept, phone) ->
                ContactCard(dept, phone)
            }
        }
    }
}

@Composable
private fun ContactCard(dept: String, phone: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(dept, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.Black.copy(alpha = 0.8f))
                Text(phone, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF00796B).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF00796B))
                }
            }
        }
    }
}
