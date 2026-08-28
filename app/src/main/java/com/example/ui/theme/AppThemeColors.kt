package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.data.model.FontScaleLevel
import com.example.data.model.ThemeType

@Immutable
data class CustomColors(
    val bg: Color,
    val cardBg: Color,
    val cardBorder: Color,
    val textMain: Color,
    val textMuted: Color,
    val headerBg: Color,
    val headerBorder: Color,
    val accent: Color,
    val accentGlow: Color,
    val btnBg: Color,
    val btnActive: Color,
    val logBg: Color,
    val logText: Color,
    val logBorder: Color,
    val highlight: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        bg = Color(0xFF090A0F),
        cardBg = Color(0xFF181A20),
        cardBorder = Color(0xFF475569),
        textMain = Color(0xFFF8FAFC),
        textMuted = Color(0xFF94A3B8),
        headerBg = Color(0xEE12141A),
        headerBorder = Color(0x6664748B),
        accent = Color(0xFFE2E8F0),
        accentGlow = Color(0x40E2E8F0),
        btnBg = Color(0xFF334155),
        btnActive = Color(0xFF475569),
        logBg = Color(0xFF090D16),
        logText = Color(0xFFE2E8F0),
        logBorder = Color(0xFF334155),
        highlight = Color(0xFFF59E0B)
    )
}

val LocalFontScale = staticCompositionLocalOf { FontScaleLevel.MEDIUM }

fun getColorsForTheme(theme: ThemeType): CustomColors {
    return when (theme) {
        ThemeType.DARK -> CustomColors(
            bg = Color(0xFF090A0F),
            cardBg = Color(0xFF181A20),
            cardBorder = Color(0xFF475569),
            textMain = Color(0xFFF8FAFC),
            textMuted = Color(0xFF94A3B8),
            headerBg = Color(0xF012141A),
            headerBorder = Color(0x6664748B),
            accent = Color(0xFFE2E8F0),
            accentGlow = Color(0x40E2E8F0),
            btnBg = Color(0xFF334155),
            btnActive = Color(0xFF475569),
            logBg = Color(0xFF090D16),
            logText = Color(0xFFE2E8F0),
            logBorder = Color(0xFF334155),
            highlight = Color(0xFFF59E0B)
        )
        ThemeType.LIGHT -> CustomColors(
            bg = Color(0xFFF1F5F9),
            cardBg = Color(0xFFFFFFFF),
            cardBorder = Color(0xFF0284C7),
            textMain = Color(0xFF0F172A),
            textMuted = Color(0xFF475569),
            headerBg = Color(0xF0FFFFFF),
            headerBorder = Color(0x4D0284C7),
            accent = Color(0xFF0284C7),
            accentGlow = Color(0x4D0284C7),
            btnBg = Color(0xFF0284C7),
            btnActive = Color(0xFF0369A1),
            logBg = Color(0xFFFFFFFF),
            logText = Color(0xFF0F172A),
            logBorder = Color(0xFFCBD5E1),
            highlight = Color(0xFFD97706)
        )
        ThemeType.GAMER -> CustomColors(
            bg = Color(0xFF000000),
            cardBg = Color(0xFF0A0A0A),
            cardBorder = Color(0xFF00FF00),
            textMain = Color(0xFF00FF00),
            textMuted = Color(0xFF00AA00),
            headerBg = Color(0xEB000A00),
            headerBorder = Color(0x6600FF00),
            accent = Color(0xFF00FF00),
            accentGlow = Color(0x6600FF00),
            btnBg = Color(0xFF005500),
            btnActive = Color(0xFF007700),
            logBg = Color(0xFF000000),
            logText = Color(0xFF00FF00),
            logBorder = Color(0xFF007700),
            highlight = Color(0xFF00FF00)
        )
        ThemeType.CYBER_YELLOW -> CustomColors(
            bg = Color(0xFF0C0C0C),
            cardBg = Color(0xFF161616),
            cardBorder = Color(0xFFFFD700),
            textMain = Color(0xFFFFD700),
            textMuted = Color(0xFFB39700),
            headerBg = Color(0xEB141400),
            headerBorder = Color(0x66FFD700),
            accent = Color(0xFFFFD700),
            accentGlow = Color(0x66FFD700),
            btnBg = Color(0xFFB8960F),
            btnActive = Color(0xFF997A0C),
            logBg = Color(0xFF0C0C0C),
            logText = Color(0xFFFFD700),
            logBorder = Color(0xFF998100),
            highlight = Color(0xFFFFD700)
        )
        ThemeType.CYBERPUNK -> CustomColors(
            bg = Color(0xFF180018),
            cardBg = Color(0xFF2B002B),
            cardBorder = Color(0xFFFF00FF),
            textMain = Color(0xFFFF00FF),
            textMuted = Color(0xFFD900D9),
            headerBg = Color(0xEB280028),
            headerBorder = Color(0x66FF00FF),
            accent = Color(0xFFFF00FF),
            accentGlow = Color(0x66FF00FF),
            btnBg = Color(0xFFB300B3),
            btnActive = Color(0xFF800080),
            logBg = Color(0xFF180018),
            logText = Color(0xFFFF00FF),
            logBorder = Color(0xFFAA00AA),
            highlight = Color(0xFFFF00FF)
        )
        ThemeType.NORDIC -> CustomColors(
            bg = Color(0xFF05131E),
            cardBg = Color(0xFF0B2136),
            cardBorder = Color(0xFF38BDF8),
            textMain = Color(0xFFF0F9FF),
            textMuted = Color(0xFF7DD3FC),
            headerBg = Color(0xF0071829),
            headerBorder = Color(0x8038BDF8),
            accent = Color(0xFF38BDF8),
            accentGlow = Color(0x8038BDF8),
            btnBg = Color(0xFF0284C7),
            btnActive = Color(0xFF0369A1),
            logBg = Color(0xFF05131E),
            logText = Color(0xFF38BDF8),
            logBorder = Color(0xFF0B2136),
            highlight = Color(0xFF38BDF8)
        )
        ThemeType.DRACULA -> CustomColors(
            bg = Color(0xFF1E1E2E),
            cardBg = Color(0xFF282A36),
            cardBorder = Color(0xFFBD93F9),
            textMain = Color(0xFFF8F8F2),
            textMuted = Color(0xFF9BA9D9),
            headerBg = Color(0xEB282A36),
            headerBorder = Color(0x66BD93F9),
            accent = Color(0xFFBD93F9),
            accentGlow = Color(0x66BD93F9),
            btnBg = Color(0xFF6272A4),
            btnActive = Color(0xFF44475A),
            logBg = Color(0xFF1E1E2E),
            logText = Color(0xFFBD93F9),
            logBorder = Color(0xFF6272A4),
            highlight = Color(0xFFBD93F9)
        )
        ThemeType.SUNSET -> CustomColors(
            bg = Color(0xFF1A0C00),
            cardBg = Color(0xFF2E1600),
            cardBorder = Color(0xFFFFB020),
            textMain = Color(0xFFFFE5B4),
            textMuted = Color(0xFFC78028),
            headerBg = Color(0xEB1E0F00),
            headerBorder = Color(0x66FFB020),
            accent = Color(0xFFFFB020),
            accentGlow = Color(0x66FFB020),
            btnBg = Color(0xFFD9822B),
            btnActive = Color(0xFFB26218),
            logBg = Color(0xFF1A0C00),
            logText = Color(0xFFFFB020),
            logBorder = Color(0xFFA56A20),
            highlight = Color(0xFFFFB020)
        )
        ThemeType.SOLARIZED -> CustomColors(
            bg = Color(0xFF002B36),
            cardBg = Color(0xFF073642),
            cardBorder = Color(0xFF2AA198),
            textMain = Color(0xFF93A1A1),
            textMuted = Color(0xFF657B83),
            headerBg = Color(0xEB002B36),
            headerBorder = Color(0x662AA198),
            accent = Color(0xFF2AA198),
            accentGlow = Color(0x662AA198),
            btnBg = Color(0xFF268BD2),
            btnActive = Color(0xFF206B9E),
            logBg = Color(0xFF002B36),
            logText = Color(0xFF2AA198),
            logBorder = Color(0xFF586E75),
            highlight = Color(0xFF2AA198)
        )
    }
}
