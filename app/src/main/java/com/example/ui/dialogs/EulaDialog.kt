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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

@Composable
fun EulaDialog(
    onAgree: () -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor

    Dialog(
        onDismissRequest = { onDismiss?.invoke() },
        properties = DialogProperties(dismissOnBackPress = onDismiss != null, dismissOnClickOutside = false)
    ) {
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
                text = "TERMS OF USE & LIMITATION OF LIABILITY (v1.50)",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 6.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Morphs Creations Image Identifier Suite",
                        color = customColors.highlight,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "1. GENERAL INFORMATIONAL PURPOSE ONLY",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "This application utilizes artificial intelligence and automated visual recognition algorithms to analyze images. All results, identification labels, biographies, and associated metadata provided are generated for general informational, educational, and entertainment purposes only.",
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = "2. DISCLAIMER OF ACCURACY & MISIDENTIFICATION",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Computer vision and AI identification technology are inherently imperfect and subject to error, misinterpretation, and false positives. The author/developer makes no guarantees or representations regarding the accuracy, completeness, or reliability of results. Incorrect identification cannot and shall not be held against the author.",
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        lineHeight = 16.sp
                    )

                    // Critical Medical Warning Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33EF4444))
                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "3. STRICT MEDICAL & PHARMACEUTICAL DISCLAIMER",
                            color = Color(0xFFFCA5A5),
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "CRITICAL WARNING: THIS APPLICATION IS NOT A MEDICAL DEVICE AND IS NOT DESIGNED, CERTIFIED, OR INTENDED TO IDENTIFY MEDICATIONS, PILLS, PHARMACEUTICALS, CHEMICALS, TOXIC SUBSTANCES, OR MEDICAL CONDITIONS. ALWAYS CONSULT A QUALIFIED MEDICAL PROFESSIONAL OR PHARMACIST.",
                            color = Color(0xFFFCA5A5),
                            fontSize = (baseFontSize.value - 0.5f).sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Text(
                        text = "4. LIMITATION OF LIABILITY",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, IN NO EVENT SHALL THE AUTHOR OR DEVELOPER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OF THIS APPLICATION.",
                        color = customColors.textMuted,
                        fontSize = (baseFontSize.value - 1f).sp,
                        lineHeight = 14.sp
                    )
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
                        .clickable { onAgree() }
                        .padding(vertical = 12.dp)
                        .testTag("eula_agree_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "I Agree & Accept Terms",
                        color = Color.White,
                        fontSize = (baseFontSize.value + 1f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (onDismiss != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.btnBg)
                            .clickable { onDismiss() }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
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
    }
}
