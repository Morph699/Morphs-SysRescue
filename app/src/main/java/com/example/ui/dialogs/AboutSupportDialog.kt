package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale

@Composable
fun AboutSupportDialog(
    isAboutMode: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
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
                text = if (isAboutMode) "ABOUT MORPHS CREATIONS (v1.50)" else "CONTACT & DEVELOPER SUPPORT",
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
                    .heightIn(max = 380.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isAboutMode) {
                    Text(
                        text = "Morphs Creations Image Identifier v1.50 is an ultra-fast, high-performance native visual intelligence suite engineered with Android CameraX, multimodal neural vision, and offline heuristic verification.",
                        color = customColors.textMain,
                        fontSize = baseFontSize,
                        lineHeight = 16.sp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Key Architectures & Features:", color = customColors.accent, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                        Text(text = "• 100% Native Kotlin & Jetpack Compose Pipeline\n• Real-Time CameraX Viewfinder with Pinch-to-Zoom & Torch\n• Visual AI Multimodal Analysis & Offline Heuristic Fallback\n• 'Is It Fake?' Authenticity & Vector Inspection HUD\n• 'What's It Worth?' Secondary Market Valuation\n• Scan to Re-Imagine 4K AI Art Generator\n• 9 High-Contrast Themes & Accessible Font Scaling\n• Room Database Session Logs with CSV/JSON Export", color = customColors.textMuted, fontSize = (baseFontSize.value - 1f).sp, lineHeight = 15.sp)
                    }

                    Text(
                        text = "Crafted with dedication by Morphs Creations.",
                        color = customColors.highlight,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "Need assistance, have feature ideas, or want to report a misidentification?",
                        color = customColors.textMain,
                        fontSize = baseFontSize
                    )

                    // Support Email Card
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
                            Text(text = "Official Support Email", color = customColors.textMuted, fontSize = (baseFontSize.value - 2f).sp)
                            Text(text = "morph6969@gmail.com", color = customColors.accent, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(customColors.btnBg)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString("morph6969@gmail.com"))
                                    Toast.makeText(context, "Email copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Copy", color = Color.White, fontSize = (baseFontSize.value - 1.5f).sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Direct Send Email Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7))
                            .clickable {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:morph6969@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Morphs Image Identifier Support Request")
                                }
                                try {
                                    context.startActivity(emailIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("send_email_support_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📧 Compose Support Email",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Support Creator Links
                    Text(
                        text = "Tip & Support Creator:",
                        color = customColors.accent,
                        fontSize = baseFontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF13C3FF))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com"))
                                    context.startActivity(intent)
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("☕ Ko-fi Tip", color = Color.Black, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF00457C))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me"))
                                    context.startActivity(intent)
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💙 PayPal", color = Color.White, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                        }
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
