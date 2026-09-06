package com.example.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val WarmSandColorScheme = lightColorScheme(
    primary = ElectricianOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = WarmSandSurfaceVariant,
    onPrimaryContainer = ElectricianOrangeDark,
    secondary = ElectricianAmber,
    onSecondary = TextDeepBrown,
    secondaryContainer = WarmSandSurfaceContainer,
    onSecondaryContainer = TextDeepBrown,
    tertiary = ElectricianGold,
    onTertiary = TextDeepBrown,
    background = WarmSandBackground,
    onBackground = TextDeepBrown,
    surface = WarmSandSurface,
    onSurface = TextDeepBrown,
    surfaceVariant = WarmSandSurfaceVariant,
    onSurfaceVariant = TextBrownSecondary,
    outline = WarmSandBorder,
    outlineVariant = DividerWarm,
    error = StatusRejectedRed,
    onError = Color.White
)

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ElectriciansAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WarmSandColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
