package com.example.guestservicesapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {

    val logoScale = remember { Animatable(0.8f) }

    val glow by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0.95f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val textAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(900),
        label = ""
    )

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = FastOutSlowInEasing)
        )

        delay(2100)
        onDone()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6C63FF),
            Color(0xFF4E54C8),
            Color(0xFF232946)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(logoScale.value * glow)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.10f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "GS",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Guest Services",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White.copy(alpha = textAlpha),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Luxury at your fingertips",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f * textAlpha)
            )

            Spacer(modifier = Modifier.height(30.dp))

            LoadingDots()
        }
    }
}

@Composable
fun LoadingDots() {

    val infinite = rememberInfiniteTransition(label = "")

    val scale1 by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 0),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val scale2 by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val scale3 by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        Dot(scale1)
        Dot(scale2)
        Dot(scale3)
    }
}

@Composable
fun Dot(scale: Float) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimary)
    )
}