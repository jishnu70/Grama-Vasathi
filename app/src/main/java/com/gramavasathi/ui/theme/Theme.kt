package com.gramavasathi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val scheme = lightColorScheme(
    primary = EarthBrown,
    onPrimary = White,
    primaryContainer = WarmBeige,
    onPrimaryContainer = EarthBrown,
    secondary = GoldenWheat,
    onSecondary = EarthBrown,
    secondaryContainer = WarmBeige,
    tertiary = Terracotta,
    onTertiary = White,
    background = CreamWhite,
    onBackground = EarthBrown,
    surface = CreamWhite,
    onSurface = EarthBrown,
    surfaceVariant = WarmBeige,
    onSurfaceVariant = MutedBrown,
    outline = DividerWarm,
    outlineVariant = DividerWarm
)

@Composable
fun GramaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
}
