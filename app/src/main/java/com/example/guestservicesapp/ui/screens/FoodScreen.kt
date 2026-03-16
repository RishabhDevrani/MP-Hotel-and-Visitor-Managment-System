package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class FoodItem(val name: String, val price: String, val category: String)

@Composable
fun FoodScreen() {
    var showBuffet by remember { mutableStateOf(false) }

    val menuItems = listOf(
        FoodItem("Paneer Butter Masala", "₹350", "Main Course"),
        FoodItem("Chicken Biryani", "₹420", "Main Course"),
        FoodItem("Dal Makhani", "₹280", "Main Course"),
        FoodItem("Hakka Noodles", "₹310", "Chinese"),
        FoodItem("Spring Rolls", "₹220", "Starter"),
        FoodItem("Gulab Jamun", "₹120", "Dessert")
    )

    Column(Modifier.fillMaxSize().background(Color(0xFFF8F9FF)).padding(16.dp)) {
        
        /* Menu / Buffet Toggle */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(4.dp)
        ) {
            Button(
                onClick = { showBuffet = false },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showBuffet) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!showBuffet) Color.White else Color.Gray
                ),
                elevation = null
            ) {
                Text("A La Carte", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { showBuffet = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showBuffet) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (showBuffet) Color.White else Color.Gray
                ),
                elevation = null
            ) {
                Text("Buffet System", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(20.dp))

        if (showBuffet) {
            BuffetContent()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text("Today's Special", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(8.dp))
                }
                items(menuItems) { item ->
                    FoodCard(item)
                }
            }
        }
    }
}

@Composable
private fun BuffetContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Active Buffet Plans", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        
        BuffetPlanCard(
            title = "Breakfast Buffet",
            time = "07:30 AM - 10:30 AM",
            price = "₹499 per person",
            color = Color(0xFFFFA62B)
        )
        
        BuffetPlanCard(
            title = "Grand Lunch Buffet",
            time = "12:30 PM - 03:30 PM",
            price = "₹899 per person",
            color = Color(0xFF6C63FF)
        )

        BuffetPlanCard(
            title = "Dinner Buffet",
            time = "07:30 PM - 10:30 PM",
            price = "₹999 per person",
            color = Color(0xFF2EC4B6)
        )
    }
}

@Composable
private fun FoodCard(item: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(item.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(item.price, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
        }
    }
}

@Composable
private fun BuffetPlanCard(title: String, time: String, price: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = color)
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
            }
            Spacer(Modifier.height(8.dp))
            Text(time, fontWeight = FontWeight.Medium)
            Text(price, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.7f))
        }
    }
}