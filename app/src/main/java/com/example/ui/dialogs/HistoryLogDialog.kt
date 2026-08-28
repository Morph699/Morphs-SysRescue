package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.local.entity.LogRecordEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.model.AppTier
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryLogDialog(
    scans: List<ScanRecordEntity>,
    logs: List<LogRecordEntity>,
    activeTier: AppTier,
    onDeleteScan: (Long) -> Unit,
    onClearAllScans: () -> Unit,
    onClearLogs: () -> Unit,
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

    var selectedTabIndex by remember { mutableStateOf(0) } // 0: Live Logs, 1: Scan History

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
                text = "SYSTEM HISTORY & CONSOLE LOGS",
                color = customColors.accent,
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            // Tabs: Logs vs Scans
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = customColors.bg,
                contentColor = customColors.accent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = customColors.accent
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Console Logs (${logs.size})", fontSize = (baseFontSize.value - 0.5f).sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Scan Records (${scans.size})", fontSize = (baseFontSize.value - 0.5f).sp, fontWeight = FontWeight.Bold) }
                )
            }

            if (selectedTabIndex == 0) {
                // Console Log Stream
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 320.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(customColors.logBg)
                        .border(1.dp, customColors.logBorder, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(logs) { log ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = timeFormat.format(Date(log.timestamp)),
                                    color = customColors.textMuted,
                                    fontSize = (baseFontSize.value - 2f).sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = log.message,
                                    color = customColors.logText,
                                    fontSize = (baseFontSize.value - 1f).sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            } else {
                // Scans List
                if (scans.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.bg)
                            .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No saved scan records yet.",
                            color = customColors.textMuted,
                            fontSize = baseFontSize
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(scans, key = { it.id }) { scan ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(customColors.bg)
                                    .border(1.dp, customColors.cardBorder, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = scan.title,
                                        color = customColors.accent,
                                        fontSize = baseFontSize,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${scan.confidenceScore}%",
                                        color = customColors.highlight,
                                        fontSize = (baseFontSize.value - 1.5f).sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = scan.summary,
                                    color = customColors.textMain,
                                    fontSize = (baseFontSize.value - 1f).sp,
                                    lineHeight = 14.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Value: ${scan.valuationEstimate ?: "N/A"}",
                                        color = customColors.textMuted,
                                        fontSize = (baseFontSize.value - 2f).sp
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0x33EF4444))
                                            .clickable { onDeleteScan(scan.id) }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("Delete", color = Color(0xFFEF4444), fontSize = (baseFontSize.value - 2f).sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Export & Control Actions
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
                                            putExtra(Intent.EXTRA_SUBJECT, "Morphs_Scan_History.csv")
                                            putExtra(Intent.EXTRA_TEXT, csv)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Export CSV"))
                                    }
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("export_scans_csv_btn"),
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
                                            putExtra(Intent.EXTRA_SUBJECT, "Morphs_Scan_History.json")
                                            putExtra(Intent.EXTRA_TEXT, json)
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Export JSON"))
                                    }
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("export_scans_json_btn"),
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
                            .clickable {
                                if (selectedTabIndex == 0) onClearLogs() else onClearAllScans()
                            }
                            .padding(vertical = 10.dp)
                            .testTag("clear_history_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedTabIndex == 0) "Clear Logs" else "Clear All Scans",
                            color = Color.White,
                            fontSize = baseFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.btnBg)
                            .clickable { onDismiss() }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Close", color = Color.White, fontSize = baseFontSize, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
