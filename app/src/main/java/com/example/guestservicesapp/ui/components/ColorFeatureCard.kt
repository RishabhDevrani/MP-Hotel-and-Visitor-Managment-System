package com.example.guestservicesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ColorFeatureCard(
    title: String,
    icon: @Composable () -> Unit,
    accent: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    // More vibrant gradient: Accent to a very soft tint of the accent
    val gradient = Brush.linearGradient(
        colors = listOf(
            accent.copy(alpha = 0.25f),
            accent.copy(alpha = 0.05f)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(accent.copy(alpha = 0.3f), Color.Transparent)),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /* icon container - More defined with elevation and stronger colors */
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = accent,
                tonalElevation = 4.dp,
                modifier = Modifier.size(46.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        icon()
                    }
                }
            }

            /* Title - Now has full width of card minus padding */
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}
