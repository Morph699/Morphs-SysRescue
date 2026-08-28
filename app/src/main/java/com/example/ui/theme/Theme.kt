package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.data.model.FontScaleLevel
import com.example.data.model.ThemeType

@Composable
fun MyApplicationTheme(
    theme: ThemeType = ThemeType.DARK,
    fontScale: FontScaleLevel = FontScaleLevel.MEDIUM,
    content: @Composable () -> Unit
) {
    val customColors = getColorsForTheme(theme)

    val colorScheme = darkColorScheme(
        primary = customColors.accent,
        onPrimary = customColors.bg,
        primaryContainer = customColors.btnBg,
        onPrimaryContainer = customColors.textMain,
        secondary = customColors.accent,
        onSecondary = customColors.bg,
        background = customColors.bg,
        onBackground = customColors.textMain,
        surface = customColors.cardBg,
        onSurface = customColors.textMain,
        surfaceVariant = customColors.cardBg,
        onSurfaceVariant = customColors.textMuted,
        outline = customColors.cardBorder
    )

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalFontScale provides fontScale
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
