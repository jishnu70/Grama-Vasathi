package com.gramavasathi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val scheme = lightColorScheme(
    primary = EarthBrown,
    secondary = GoldenWheat,
    tertiary = Terracotta,
    background = CreamWhite,
    surface = CreamWhite,
    surfaceVariant = WarmBeige
)

@Composable
fun GramaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
}
