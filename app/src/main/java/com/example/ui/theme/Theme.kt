package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CodeQuestDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),

    secondary = NeonEmerald,
    onSecondary = Color(0xFF00391A),
    secondaryContainer = Color(0xFF005328),
    onSecondaryContainer = Color(0xFF6CFF9D),

    tertiary = CyberGold,
    onTertiary = Color(0xFF3F2E00),
    tertiaryContainer = Color(0xFF5B4300),
    onTertiaryContainer = Color(0xFFFFE088),

    background = CyberBackground,
    onBackground = TextPrimary,

    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    error = CyberCrimson,
    onError = Color.White,

    outline = CyberBorder,
    outlineVariant = CyberSurfaceLight
)

@Composable
fun CodeQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // CodeQuest is locked to the high-contrast cyber dark mode for kid-friendly visual appeal
    MaterialTheme(
        colorScheme = CodeQuestDarkColorScheme,
        typography = Typography,
        content = content
    )
}
