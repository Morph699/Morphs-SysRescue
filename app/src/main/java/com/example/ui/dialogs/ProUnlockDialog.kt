package com.example.ui.dialogs

import android.widget.Toast
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppTier
import com.example.data.model.LicenseKeyValidator
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

@Composable
fun ProUnlockDialog(
    activeTier: AppTier,
    activeKey: String?,
    onActivateKey: (String) -> Boolean,
    onUnlockTierDirectly: (AppTier) -> Unit,
    onDeactivateLicense: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor

    var inputKey by remember { mutableStateOf(activeKey ?: "") }

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
                text = "👑 UPGRADE & LICENSE MANAGER",
                color = Color(0xFFF59E0B),
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
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Active Tier Status Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.accent, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Current Status", color = customColors.textMuted, fontSize = (baseFontSize.value - 2f).sp)
                        Text(text = activeTier.title, color = customColors.highlight, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }

                    if (activeKey != null) {
                        Text(
                            text = activeKey,
                            color = customColors.accent,
                            fontSize = (baseFontSize.value - 1.5f).sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 12-character Key Validation Input
                Text(
                    text = "Enter 12-Character Mathematical Key:",
                    color = customColors.accent,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = inputKey,
                    onValueChange = { inputKey = it.uppercase() },
                    placeholder = { Text("e.g. LIFETIME_KEY", color = customColors.textMuted, fontSize = baseFontSize) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("license_key_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.accent,
                        unfocusedBorderColor = customColors.cardBorder,
                        focusedTextColor = customColors.textMain,
                        unfocusedTextColor = customColors.textMain,
                        focusedContainerColor = customColors.bg,
                        unfocusedContainerColor = customColors.bg
                    ),
                    singleLine = true
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0284C7))
                        .clickable {
                            if (inputKey.isNotBlank()) {
                                onActivateKey(inputKey)
                            } else {
                                Toast.makeText(context, "Please enter a 12-character key", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(vertical = 10.dp)
                        .testTag("activate_key_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Validate & Activate Key",
                        color = Color.White,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Instant Tier Auto-Key Fillers (for instant access)
                Text(
                    text = "Instant Access / Tier Auto-Keys:",
                    color = customColors.textMuted,
                    fontSize = (baseFontSize.value - 1f).sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Supporter ($1.50)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(customColors.btnBg)
                            .clickable {
                                val key = LicenseKeyValidator.generateValidKey(AppTier.SUPPORTER)
                                inputKey = key
                                onUnlockTierDirectly(AppTier.SUPPORTER)
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⭐ Supporter\n($1.50)", color = Color.White, fontSize = 9.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }

                    // Pro ($3.50)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0284C7))
                            .clickable {
                                val key = LicenseKeyValidator.generateValidKey(AppTier.PRO)
                                inputKey = key
                                onUnlockTierDirectly(AppTier.PRO)
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑 Pro\n($3.50)", color = Color.White, fontSize = 9.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }

                    // Lifetime ($5.00)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFD97706))
                            .clickable {
                                val key = LicenseKeyValidator.generateValidKey(AppTier.LIFETIME)
                                inputKey = key
                                onUnlockTierDirectly(AppTier.LIFETIME)
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑 Lifetime\n($5.00)", color = Color.White, fontSize = 9.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }
                }

                // Tier Feature Comparison Matrix
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "👑 Lifetime Edition ($5.00)", color = Color(0xFFF59E0B), fontSize = (baseFontSize.value - 1f).sp, fontWeight = FontWeight.Bold)
                    Text(text = "• All 9 High-Contrast Visual Themes\n• Full Typography Font Scaling\n• 🎬 Batch Scan Queue & Rapid Camera\n• 📋 CSV & JSON History Exporting\n• 🏷️ What's It Worth? Market Appraisal Mode\n• 🎨 Scan to Re-Imagine 4K AI Masterpiece Art", color = customColors.textMain, fontSize = (baseFontSize.value - 1.5f).sp, lineHeight = 13.sp)
                }

                // Reset License Option
                if (activeTier != AppTier.FREE) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33EF4444))
                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(8.dp))
                            .clickable { onDeactivateLicense() }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Reset License to Free Tier", color = Color(0xFFEF4444), fontSize = (baseFontSize.value - 1f).sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(customColors.btnBg)
                    .clickable { onDismiss() }
                    .padding(vertical = 10.dp),
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
