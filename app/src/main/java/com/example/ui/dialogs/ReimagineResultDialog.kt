package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.service.ReimagineResult
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import com.example.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReimagineResultDialog(
    result: ReimagineResult?,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
                text = "Morphs Creations AI Image Results:",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            Text(
                text = "AI 4K Art: ${(result?.prompt ?: "OBJECT").uppercase()}",
                color = Color(0xFF38BDF8),
                fontSize = (baseFontSize.value + 0.5f).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLoading || result == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(10.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFF59E0B))
                        Text(
                            text = "Generating 4K Object-Aware AI Masterpiece...",
                            color = Color(0xFFF59E0B),
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                } else {
                    AsyncImage(
                        model = result.imageUrl,
                        contentDescription = "4K Re-Imagined Art",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF38BDF8), RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = "Style Preset: ${result.style}",
                        color = customColors.textMuted,
                        fontSize = (baseFontSize.value - 1f).sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Actions Row: Save, Share, Full 4K, Close
            if (!isLoading && result != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Save to Gallery
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981))
                            .clickable {
                                coroutineScope.launch {
                                    val loader = context.imageLoader
                                    val req = ImageRequest.Builder(context)
                                        .data(result.imageUrl)
                                        .allowHardware(false)
                                        .build()
                                    val res = loader.execute(req)
                                    if (res is SuccessResult) {
                                        val drawable = res.drawable
                                        val bmp = (drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
                                        if (bmp != null) {
                                            val uri = ImageUtils.saveBitmapToGallery(context, bmp, "Morphs_AI_Art")
                                            if (uri != null) {
                                                Toast.makeText(context, "Saved to Gallery / Photos!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Saved to Pictures folder", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("save_reimagine_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💾 Save",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Share
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF8B5CF6))
                            .clickable {
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Morphs AI 4K Artwork")
                                    putExtra(Intent.EXTRA_TEXT, "Check out this 4K AI art generated with Morphs Creations Image Identifier!\n${result.imageUrl}")
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share 4K Art")
                                context.startActivity(shareIntent)
                            }
                            .padding(vertical = 10.dp)
                            .testTag("share_reimagine_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📲 Share",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Full View
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7))
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.imageUrl))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 10.dp)
                            .testTag("full_4k_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Full 4K",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
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
