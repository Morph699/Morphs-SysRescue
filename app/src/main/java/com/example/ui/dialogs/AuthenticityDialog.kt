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
import com.example.service.AnalysisResult
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

@Composable
fun AuthenticityDialog(
    result: AnalysisResult,
    onDismiss: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current
    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 14.sp * fontScale.scaleFactor

    val isAuthentic = result.authenticityVerdict.contains("AUTHENTIC", ignoreCase = true)
    val verdictColor = if (isAuthentic) Color(0xFF38BDF8) else Color(0xFFEF4444)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.cardBg)
                .border(1.dp, customColors.cardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Morphs Creations AI Image Results:",
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
                    .heightIn(max = 380.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Verdict Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(verdictColor.copy(alpha = 0.1f))
                        .border(1.5.dp, verdictColor, RoundedCornerShape(10.dp))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = result.authenticityVerdict,
                        color = verdictColor,
                        fontSize = (headerFontSize.value + 1.5f).sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${result.confidenceScore}% Confidence Score",
                        color = Color(0xFFF59E0B),
                        fontSize = (baseFontSize.value + 1f).sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Stitching, proportions, materials, and visual logo vectors match official production specifications.",
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Vector Checklist
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Authenticity Matrix Inspection:",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result.details,
                        color = customColors.textMuted,
                        fontSize = baseFontSize,
                        lineHeight = 16.sp
                    )
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
                    .testTag("close_authenticity_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Close Scanner",
                    color = Color.White,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
