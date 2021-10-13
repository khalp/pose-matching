package com.microsoft.device.display.samples.posematching.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = OrangeDark,
    primaryVariant = OrangeMain,
    onPrimary = White,
    secondary = BlushDark,
    secondaryVariant = BlushMain,
    onSecondary = White,
    surface = DarkGray,
    background = DarkGray,
    onSurface = White,
    onBackground = White,
)

private val LightColorPalette = lightColors(
    primary = OrangeMain,
    primaryVariant = OrangeDark,
    onPrimary = Black,
    secondary = BlushMain,
    secondaryVariant = BlushDark,
    onSecondary = Black,
    surface = BlushLight,
    background = BlushLight,
    onSurface = Black,
    onBackground = Black,
)

@Composable
fun PoseMatchingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}