package com.example.guestservicesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/* ---------- DARK THEME ---------- */

private val DarkColorScheme = darkColorScheme(

    primary = HotelPurple,
    secondary = HotelTeal,
    tertiary = HotelGold,

    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,

    onPrimary = DarkTextPrimary,
    onSecondary = DarkTextPrimary,
    onTertiary = DarkTextPrimary,

    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

/* ---------- LIGHT THEME ---------- */

private val LightColorScheme = lightColorScheme(

    primary = HotelPurple,
    secondary = HotelTeal,
    tertiary = HotelGold,

    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,

    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

@Composable
fun GuestServicesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}