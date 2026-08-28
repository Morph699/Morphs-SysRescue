package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppTier
import com.example.data.model.FontScaleLevel
import com.example.data.model.ThemeType
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import com.example.ui.theme.getColorsForTheme

@Composable
fun ThemeSettingsDialog(
    currentTheme: ThemeType,
    currentFontScale: FontScaleLevel,
    activeTier: AppTier,
    onSelectTheme: (ThemeType) -> Unit,
    onSelectFontScale: (FontScaleLevel) -> Unit,
    onDismiss: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.cardBg)
                .border(1.dp, customColors.cardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎨 THEME & ACCESSIBILITY CONTROLS",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Section 1: 9 High-Contrast Themes
                Text(
                    text = "High-Contrast Visual Schemes:",
                    color = customColors.accent,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )

                ThemeType.entries.forEach { theme ->
                    val themeColors = getColorsForTheme(theme)
                    val isSelected = theme == currentTheme
                    val isLocked = theme.requiredTier.level > activeTier.level

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) customColors.accentGlow else customColors.bg)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) customColors.accent else customColors.cardBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectTheme(theme) }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("theme_item_${theme.key}"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Theme Color Palette Swatch
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(themeColors.bg)
                                        .border(1.dp, Color.Gray, CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(themeColors.accent)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(themeColors.highlight)
                                )
                            }

                            Text(
                                text = theme.displayName,
                                color = if (isSelected) customColors.highlight else customColors.textMain,
                                fontSize = baseFontSize,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }

                        if (isLocked) {
                            Text(
                                text = "🔒 ${theme.requiredTier.title}",
                                color = Color(0xFFF59E0B),
                                fontSize = (baseFontSize.value - 2f).sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else if (isSelected) {
                            Text(
                                text = "✓ Active",
                                color = Color(0xFF10B981),
                                fontSize = (baseFontSize.value - 1.5f).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Section 2: Font Scaling
                Text(
                    text = "Typography Font Scaling:",
                    color = customColors.accent,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FontScaleLevel.entries.forEach { scale ->
                        val isScaleSelected = scale == currentFontScale
                        val isScaleLocked = scale.requiredTier.level > activeTier.level

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isScaleSelected) customColors.accentGlow else customColors.bg)
                                .border(
                                    width = if (isScaleSelected) 1.5.dp else 1.dp,
                                    color = if (isScaleSelected) customColors.accent else customColors.cardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectFontScale(scale) }
                                .padding(vertical = 8.dp)
                                .testTag("font_scale_${scale.key}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = scale.key.uppercase(),
                                    color = if (isScaleSelected) customColors.highlight else customColors.textMain,
                                    fontSize = (baseFontSize.value - 1f).sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isScaleLocked) {
                                    Text(
                                        text = "🔒 Pro",
                                        color = Color(0xFFF59E0B),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(customColors.btnBg)
                    .clickable { onDismiss() }
                    .padding(vertical = 12.dp)
                    .testTag("close_theme_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Close",
                    color = Color.White,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
