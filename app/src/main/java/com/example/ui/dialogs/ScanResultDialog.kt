package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.service.AnalysisResult
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import java.net.URLEncoder

@Composable
fun ScanResultDialog(
    result: AnalysisResult,
    scannedBitmap: Bitmap?,
    onDismiss: () -> Unit,
    onOpenImdb: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 14.sp * fontScale.scaleFactor

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
                text = "Morphs Creations Visual AI Results",
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
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Scanned Image Thumbnail
                if (scannedBitmap != null) {
                    Image(
                        bitmap = scannedBitmap.asImageBitmap(),
                        contentDescription = "Scanned Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Title & Category Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = result.title,
                        color = customColors.accent,
                        fontSize = (headerFontSize.value + 1f).sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x3310B981))
                            .border(1.dp, Color(0xFF10B981), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${result.confidenceScore}% Match",
                            color = Color(0xFF10B981),
                            fontSize = (baseFontSize.value - 1.5f).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Category Tag
                Text(
                    text = "Category: ${result.category}",
                    color = customColors.highlight,
                    fontSize = baseFontSize,
                    fontWeight = FontWeight.SemiBold
                )

                // Summary Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Overview:",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result.summary,
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        lineHeight = 16.sp
                    )
                }

                // Details Box
                if (result.details.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Key Identifiers & Features:",
                            color = customColors.accent,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = result.details,
                            color = customColors.textMuted,
                            fontSize = baseFontSize,
                            lineHeight = 15.sp
                        )
                    }
                }

                // Price Valuation & Authenticity Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = "Est. Value", color = customColors.textMuted, fontSize = (baseFontSize.value - 2f).sp)
                        Text(text = result.valuationEstimate, color = customColors.highlight, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = "Status", color = customColors.textMuted, fontSize = (baseFontSize.value - 2f).sp)
                        Text(text = result.authenticityVerdict, color = Color(0xFF38BDF8), fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Google Lens Web / Query
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7))
                            .clickable {
                                val url = "https://lens.google.com/uploadbyurl?url=${URLEncoder.encode(result.searchQuery, "UTF-8")}&hl=en"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${URLEncoder.encode(result.searchQuery, "UTF-8")}"))
                                    context.startActivity(webIntent)
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("lens_search_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔍 Google Lens",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Copy Details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.btnBg)
                            .clickable {
                                val fullText = "${result.title}\nCategory: ${result.category}\n\n${result.summary}\n\n${result.details}\nEst. Value: ${result.valuationEstimate}"
                                clipboardManager.setText(AnnotatedString(fullText))
                                Toast.makeText(context, "Copied details to clipboard", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 10.dp)
                            .testTag("copy_details_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📋 Copy Info",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (result.imdbQuery != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF59E0B))
                            .clickable { onOpenImdb(result.imdbQuery) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎬 View '${result.imdbQuery}' on IMDb",
                            color = Color.Black,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.btnBg)
                        .clickable { onDismiss() }
                        .padding(vertical = 10.dp)
                        .testTag("close_result_btn"),
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
