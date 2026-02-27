package com.example.prueba1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoppelColorScheme = lightColorScheme(
    primary = AzulDigital,
    onPrimary = Color.White,
    secondary = AmarilloBrillante,
    onSecondary = Color.Black,
    error = RojoAlerta,
    background = BlancoFondo,
    onBackground = Color.Black,
    surface = BlancoFondo,
    onSurface = Color.Black
)

@Composable
fun Prueba1Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CoppelColorScheme,
        typography = Typography,
        content = content
    )
}