package com.jatinvashisht.ballclickergame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = colorPrimary,
    onPrimary = colorOnPrimary,
    primaryVariant = colorPrimaryVariant,
    secondary = colorPrimary,
    onSecondary = colorOnPrimary,
    surface = colorSurface,
    onSurface = colorOnPrimary,
    background = colorBackground,
)

private val LightColorPalette = lightColors(
    primary = Color(67, 160, 71, 255),
    onPrimary = Color.Black,
    primaryVariant = colorPrimaryVariant,
    secondary = Color(67, 160, 71, 255),
    onSecondary = Color.Black,
    surface = Color(216, 27, 96, 255),
    onSurface = Color.Black,
    background = Color(128, 207, 132, 255)

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BallClickerGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
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