package com.openclassroom.vitesse.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    primaryContainer = BlueGrayPrimaryVariant,
    onPrimary = WhiteOnPrimary,
    secondary = PaleGoldSecondary,
    secondaryContainer = PaleGoldSecondaryVariant,
    onSecondary = WhiteOnSecondary,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainerHigh = SurfaceContainerHigh,
    error = Error
)

@Composable
fun VitesseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
