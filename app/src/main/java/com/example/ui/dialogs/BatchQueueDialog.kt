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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.local.entity.BatchItemEntity
import com.example.data.model.AppTier
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BatchQueueDialog(
    batchQueue: List<BatchItemEntity>,
    activeTier: AppTier,
    onInspectLens: (BatchItemEntity) -> Unit,
    onInspectImdb: (BatchItemEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    onClearAll: () -> Unit,
    onExportCsv: suspend () -> String,
    onExportJson: suspend () -> String,
    onOpenPro: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

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
                text = "🎬 BATCH SCAN QUEUE CONSOLE",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            Text(
                text = "Inspect images one-by-one or export full session logs.",
                color = customColors.textMuted,
                fontSize = (baseFontSize.value - 1f).sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (batchQueue.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(customColors.bg)
                        .border(1.dp, customColors.cardBorder, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No frames captured in queue yet.\nSelect Batch Scan Queue and tap the shutter to snap items!",
                        color = customColors.textMuted,
                        fontSize = baseFontSize,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(batchQueue, key = { _, item -> item.id }) { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(customColors.bg)
                                .border(1.dp, customColors.cardBorder, RoundedCornerShape(10.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = File(item.imagePath),
                                contentDescription = "Snap #${index + 1}",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, customColors.accent, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Frame #${index + 1} • ${timeFormat.format(Date(item.timestamp))}",
                                        color = customColors.accent,
                                        fontSize = (baseFontSize.value - 0.5f).sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    if (item.isViewed) {
                                        Text(
                                            text = "✓ Viewed",
                                            color = Color(0xFF10B981),
                                            fontSize = (baseFontSize.value - 2f).sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(customColors.accentGlow)
                                            .border(1.dp, customColors.accent, RoundedCornerShape(6.dp))
                                            .clickable { onInspectLens(item) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🔍 Lens", color = customColors.accent, fontSize = (baseFontSize.value - 1.5f).sp, fontWeight = FontWeight.Bold)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(customColors.accentGlow)
                                            .border(1.dp, customColors.accent, RoundedCornerShape(6.dp))
                                            .clickable { onInspectImdb(item) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🎬 IMDb", color = customColors.accent, fontSize = (baseFontSize.value - 1.5f).sp, fontWeight = FontWeight.Bold)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0x33EF4444))
                                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(6.dp))
                                            .clickable { onDeleteItem(item.id) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🗑️ Del", color = Color(0xFFEF4444), fontSize = (baseFontSize.value - 1.5f).sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Export & Action Grid
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
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0284C7))
                            .clickable {
                                if (activeTier.level < AppTier.PRO.level) {
                                    onOpenPro()
                                } else {
                                    coroutineScope.launch {
                                        val csv = onExportCsv()
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_SUBJECT, "Morphs_Batch_Queue.csv")
                                            putExtra(Intent.EXTRA_TEXT, csv)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Export CSV"))
                                    }
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("export_batch_csv_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📋 🔒 Export CSV", color = Color.White, fontSize = (baseFontSize.value - 1f).sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF8B5CF6))
                            .clickable {
                                if (activeTier.level < AppTier.PRO.level) {
                                    onOpenPro()
                                } else {
                                    coroutineScope.launch {
                                        val json = onExportJson()
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_SUBJECT, "Morphs_Batch_Queue.json")
                                            putExtra(Intent.EXTRA_TEXT, json)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Export JSON"))
                                    }
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("export_batch_json_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📋 🔒 Export JSON", color = Color.White, fontSize = (baseFontSize.value - 1f).sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEF4444))
                            .clickable { onClearAll() }
                            .padding(vertical = 10.dp)
                            .testTag("clear_batch_queue_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🗑️ Clear All", color = Color.White, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.btnBg)
                            .clickable { onDismiss() }
                            .padding(vertical = 10.dp)
                            .testTag("return_camera_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Return to Camera", color = Color.White, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
