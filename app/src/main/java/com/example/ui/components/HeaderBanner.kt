package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppTier
import com.example.data.model.ScanMode
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

@Composable
fun HeaderBanner(
    currentMode: ScanMode,
    activeTier: AppTier,
    onModeSelected: (ScanMode) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenTheme: () -> Unit,
    onOpenEula: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenImdb: () -> Unit,
    onOpenPro: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.sp * fontScale.scaleFactor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(customColors.headerBg)
                .border(1.dp, customColors.headerBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Title
            Text(
                text = "Morphs Creations Image Identifier v1.50",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
                    .testTag("app_header_title")
            )

            // Row 1: Mode Dropdown & Nav Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown Button
                Box(
                    modifier = Modifier
                        .weight(1.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.cardBg)
                        .border(1.dp, customColors.accent, RoundedCornerShape(8.dp))
                        .clickable { isDropdownExpanded = true }
                        .padding(horizontal = 6.dp, vertical = 7.dp)
                        .testTag("mode_selector_dropdown")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentMode.displayName,
                            color = customColors.accent,
                            fontSize = (baseFontSize.value - 1f).sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Mode",
                            tint = customColors.accent
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .background(customColors.cardBg)
                            .border(1.dp, customColors.cardBorder)
                    ) {
                        ScanMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = mode.displayName,
                                        color = if (mode == currentMode) customColors.highlight else customColors.textMain,
                                        fontSize = baseFontSize,
                                        fontWeight = if (mode == currentMode) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    isDropdownExpanded = false
                                    onModeSelected(mode)
                                },
                                modifier = Modifier.testTag("mode_option_${mode.id}")
                            )
                        }
                    }
                }

                // Nav Button: Log
                HeaderNavButton(
                    text = "📋 Log",
                    onClick = onOpenHistory,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_history_btn"
                )

                // Nav Button: Theme
                HeaderNavButton(
                    text = "🎨 Theme",
                    onClick = onOpenTheme,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_theme_btn"
                )

                // Nav Button: Terms
                HeaderNavButton(
                    text = "⚖️ Terms",
                    onClick = onOpenEula,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_terms_btn"
                )
            }

            // Row 2: Secondary Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderNavButton(
                    text = "ℹ️ About",
                    onClick = onOpenAbout,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_about_btn"
                )

                HeaderNavButton(
                    text = "💬 Support",
                    onClick = onOpenSupport,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_support_btn"
                )

                HeaderNavButton(
                    text = "🎬 IMDb",
                    onClick = onOpenImdb,
                    modifier = Modifier.weight(1f),
                    testTag = "nav_imdb_btn"
                )

                // Unlock / Tier Badge Button
                val (proBtnText, proBtnBorderColor, proBtnTextColor) = when (activeTier) {
                    AppTier.LIFETIME -> Triple("👑 Lifetime", Color(0xFFF59E0B), Color(0xFFF59E0B))
                    AppTier.PRO -> Triple("👑 Pro", Color(0xFF38BDF8), Color(0xFF38BDF8))
                    AppTier.SUPPORTER -> Triple("⭐ Upgrade", Color(0xFFF59E0B), Color(0xFFF59E0B))
                    AppTier.FREE -> Triple("🔑 Unlock", Color(0xFFF59E0B), Color(0xFFF59E0B))
                }

                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0x33F59E0B))
                        .border(1.dp, proBtnBorderColor, RoundedCornerShape(7.dp))
                        .clickable { onOpenPro() }
                        .padding(horizontal = 4.dp, vertical = 7.dp)
                        .testTag("nav_unlock_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = proBtnText,
                        color = proBtnTextColor,
                        fontSize = (baseFontSize.value - 1.5f).sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderNavButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    val fontSize = (10.5.sp * fontScale.scaleFactor)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(customColors.accentGlow)
            .border(1.dp, customColors.accent.copy(alpha = 0.7f), RoundedCornerShape(7.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 7.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = customColors.accent,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
