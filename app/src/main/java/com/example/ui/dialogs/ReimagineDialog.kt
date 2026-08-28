package com.example.ui.dialogs

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

val REIMAGINE_STYLES = listOf(
    "dark knight gothic superhero style" to "1. Dark Knight / Gothic Superhero (FREE)",
    "cyberpunk futuristic neon style" to "2. Cyberpunk Futuristic City (FREE)",
    "anime manga cell-shaded style" to "3. Anime / Manga Cell-Shaded (FREE)",
    "claymation stop-motion style" to "4. Claymation Stop-Motion (FREE)",
    "80s synthwave retro glow style" to "5. 🔒 80s Synth-Wave Retro (Pro+ $3.50)",
    "medieval knight fantasy style" to "6. 🔒 Medieval Knight / Historic (Pro+ $3.50)",
    "1920s gangster noir style" to "7. 🔒 1920s Gangster Vintage (Lifetime $5.00)",
    "cosmic astral mythic masterpiece style" to "8. 🔒 Cosmic Astral Mythic (Lifetime $5.00)"
)

@Composable
fun ReimagineDialog(
    capturedBitmap: Bitmap?,
    onGenerate: (objectTag: String, stylePreset: String) -> Unit,
    onDismiss: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor

    var objectTag by remember { mutableStateOf("") }
    var selectedStyleIndex by remember { mutableStateOf(1) } // Default Cyberpunk
    var isStyleDropdownOpen by remember { mutableStateOf(false) }

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
                text = "SCAN TO RE-IMAGINE OBJECT AI (2048p 4K)",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Captured Object Frame",
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "👑 Lifetime: Unlimited 4K",
                        color = Color(0xFFF59E0B),
                        fontSize = (baseFontSize.value - 1.5f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Thumbnail preview
                if (capturedBitmap != null) {
                    Image(
                        bitmap = capturedBitmap.asImageBitmap(),
                        contentDescription = "Frame Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "Object Description / Name:",
                    color = customColors.accent,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = objectTag,
                    onValueChange = { objectTag = it },
                    placeholder = { Text("Type object description (e.g. vintage camera)", color = customColors.textMuted, fontSize = baseFontSize) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reimagine_object_input"),
                    shape = RoundedCornerShape(10.dp),
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

                Text(
                    text = "Target AI Style Preset:",
                    color = customColors.accent,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )

                // Style Dropdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.cardBorder, RoundedCornerShape(10.dp))
                        .clickable { isStyleDropdownOpen = true }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .testTag("reimagine_style_dropdown")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = REIMAGINE_STYLES[selectedStyleIndex].second,
                            color = customColors.accent,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Style",
                            tint = customColors.accent
                        )
                    }

                    DropdownMenu(
                        expanded = isStyleDropdownOpen,
                        onDismissRequest = { isStyleDropdownOpen = false },
                        modifier = Modifier
                            .background(customColors.cardBg)
                            .border(1.dp, customColors.cardBorder)
                    ) {
                        REIMAGINE_STYLES.forEachIndexed { index, pair ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = pair.second,
                                        color = if (index == selectedStyleIndex) customColors.highlight else customColors.textMain,
                                        fontSize = baseFontSize,
                                        fontWeight = if (index == selectedStyleIndex) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    selectedStyleIndex = index
                                    isStyleDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0284C7))
                        .clickable {
                            val presetValue = REIMAGINE_STYLES[selectedStyleIndex].first
                            onGenerate(objectTag, presetValue)
                        }
                        .padding(vertical = 12.dp)
                        .testTag("generate_4k_art_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Generate 4K Art",
                        color = Color.White,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.btnBg)
                        .clickable { onDismiss() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.White,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
