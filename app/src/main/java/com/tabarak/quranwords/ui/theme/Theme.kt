package com.tabarak.quranwords.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    secondary = GoldAccent,
    background = LightBackground,
    surface = LightSurface,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = Color.Black,
    secondary = GoldAccentDark,
    background = DarkBackground,
    surface = DarkSurface,
    error = ErrorRed
)

@Composable
fun TabarakWordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
