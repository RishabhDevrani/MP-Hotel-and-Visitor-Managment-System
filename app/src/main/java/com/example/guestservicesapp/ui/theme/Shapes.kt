package com.example.guestservicesapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(

    /* Small components (chips, small buttons) */
    small = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 10.dp,
        bottomEnd = 10.dp,
        bottomStart = 10.dp
    ),

    /* Medium components (cards, list items) */
    medium = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomEnd = 16.dp,
        bottomStart = 16.dp
    ),

    /* Large components (feature cards, panels) */
    large = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomEnd = 24.dp,
        bottomStart = 24.dp
    ),

    /* Extra large (hero cards like "My Stay") */
    extraLarge = RoundedCornerShape(
        topStart = 32.dp,
        topEnd = 32.dp,
        bottomEnd = 32.dp,
        bottomStart = 32.dp
    )
)