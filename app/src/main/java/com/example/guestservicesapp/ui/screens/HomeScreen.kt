package com.example.guestservicesapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guestservicesapp.data.RoomPreference
import com.example.guestservicesapp.ui.components.ColorFeatureCard
import kotlinx.coroutines.delay

private data class QuickAction(
    val title: String,
    val icon: @Composable () -> Unit,
    val accent: Color,
    val onClick: () -> Unit
)

private data class RequestItem(
    val title: String,
    val time: String,
    val status: String
)

private val AccentHousekeeping = Color(0xFF6C63FF)
private val AccentSOS = Color(0xFFFF4D6D)
private val AccentFood = Color(0xFFFFA62B)
private val AccentContact = Color(0xFF2EC4B6)

@Composable
fun HomeScreen(
    onGoHousekeeping: () -> Unit = {},
    onGoSOS: () -> Unit = {},
    onGoFood: () -> Unit = {},
    onGoContact: () -> Unit = {}
) {

    val context = LocalContext.current
    val roomNo by RoomPreference.getRoom(context).collectAsState(initial = "")

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    val checkout = "11:00 AM"

    val actions = listOf(
        QuickAction(
            "Housekeeping",
            { Icon(Icons.Filled.RoomService, null) },
            AccentHousekeeping,
            onGoHousekeeping
        ),
        QuickAction(
            "SOS",
            { Icon(Icons.Filled.NotificationsActive, null) },
            AccentSOS,
            onGoSOS
        ),
        QuickAction(
            "Food Menu",
            { Icon(Icons.Filled.Restaurant, null) },
            AccentFood,
            onGoFood
        ),
        QuickAction(
            "Contact",
            { Icon(Icons.Filled.Info, null) },
            AccentContact,
            onGoContact
        )
    )

    val recentRequests = listOf(
        RequestItem("Towels", "10:40 AM", "Pending"),
        RequestItem("Room Cleaning", "Yesterday", "Completed"),
        RequestItem("Water Bottles", "Yesterday", "In Progress")
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8F9FF),
            Color.White
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically { -40 }
            ) {

                Column {

                    Text(
                        "Welcome Guest",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        fontWeight = FontWeight.Black,
                        color = Color.Black.copy(alpha = 0.9f)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Room ${if (roomNo.isEmpty()) "6269" else roomNo} • Checkout $checkout",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }
        }

        item {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically { 60 }
            ) {
                MyStayCard(if (roomNo.isEmpty()) "6269" else roomNo, checkout)
            }
        }

        item {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn()
            ) {
                SectionHeader("Quick Services")
            }
        }

        item {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically { 80 }
            ) {
                QuickGrid(actions)
            }
        }

        item {
            SectionHeader("Recent Requests")
        }

        items(recentRequests) {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically { 100 }
            ) {
                RequestRowCard(it)
            }
        }
        
        item { Spacer(Modifier.height(20.dp)) }
    }
}

@Composable
private fun MyStayCard(roomNo: String, checkout: String) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F9))
    ) {

        Column(Modifier.padding(24.dp)) {

            Text(
                "My Stay",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                StayInfoBlock("Room", roomNo)
                StayInfoBlock("Checkout", checkout)

            }
        }
    }
}

@Composable
private fun StayInfoBlock(label: String, value: String) {

    Column {

        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.4f)
        )

        Spacer(Modifier.height(2.dp))

        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {

    Text(
        title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Black,
        color = Color.Black.copy(alpha = 0.9f)
    )
}

@Composable
private fun QuickGrid(actions: List<QuickAction>) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Box(Modifier.weight(1f)) {
                ColorFeatureCard(
                    title = actions[0].title,
                    icon = actions[0].icon,
                    accent = actions[0].accent,
                    onClick = actions[0].onClick
                )
            }
            Box(Modifier.weight(1f)) {
                ColorFeatureCard(
                    title = actions[1].title,
                    icon = actions[1].icon,
                    accent = actions[1].accent,
                    onClick = actions[1].onClick
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Box(Modifier.weight(1f)) {
                ColorFeatureCard(
                    title = actions[2].title,
                    icon = actions[2].icon,
                    accent = actions[2].accent,
                    onClick = actions[2].onClick
                )
            }
            Box(Modifier.weight(1f)) {
                ColorFeatureCard(
                    title = actions[3].title,
                    icon = actions[3].icon,
                    accent = actions[3].accent,
                    onClick = actions[3].onClick
                )
            }
        }
    }
}

@Composable
private fun RequestRowCard(req: RequestItem) {

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FB))
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {

                Text(
                    req.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    req.time,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.4f)
                )
            }

            StatusChip(req.status)
        }
    }
}

@Composable
private fun StatusChip(status: String) {

    val color = when (status.lowercase()) {
        "pending" -> Color(0xFFFFA62B)
        "in progress" -> Color(0xFF6C63FF)
        "completed" -> Color(0xFF2EC4B6)
        else -> Color.Gray
    }

    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp)
    ) {

        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
