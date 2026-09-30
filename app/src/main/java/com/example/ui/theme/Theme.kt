package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF93C5FD),
    onPrimary = Color(0xFF001E4D),
    primaryContainer = PrimaNavyContainer,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF60A5FA),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color.White,
    tertiary = PrimaTertiaryFixedDim,
    onTertiary = Color.Black,
    tertiaryContainer = PrimaTertiaryContainer,
    onTertiaryContainer = Color.White,
    background = Color(0xFF0B132B),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFF475569),
    error = Color(0xFFF87171),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaNavy,
    onPrimary = Color.White,
    primaryContainer = PrimaNavyContainer,
    onPrimaryContainer = Color.White,
    secondary = PrimaSecondary,
    onSecondary = Color.White,
    secondaryContainer = PrimaSecondaryContainer,
    onSecondaryContainer = Color.White,
    tertiary = PrimaTertiary,
    onTertiary = Color.White,
    tertiaryContainer = PrimaTertiaryContainer,
    onTertiaryContainer = Color.White,
    background = PrimaSurface,
    onBackground = PrimaOnSurface,
    surface = PrimaSurface,
    onSurface = PrimaOnSurface,
    surfaceVariant = PrimaSurfaceContainerHigh,
    onSurfaceVariant = PrimaOnSurfaceVariant,
    outline = PrimaOutline,
    outlineVariant = PrimaOutlineVariant,
    error = PrimaError,
    onError = PrimaOnError,
    errorContainer = PrimaErrorContainer,
    onErrorContainer = PrimaOnErrorContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
