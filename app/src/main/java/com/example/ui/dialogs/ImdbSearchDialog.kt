package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import java.net.URLEncoder

@Composable
fun ImdbSearchDialog(
    initialQuery: String = "",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    val baseFontSize = 11.5.sp * fontScale.scaleFactor
    val headerFontSize = 13.5.sp * fontScale.scaleFactor

    var actorQuery by remember { mutableStateOf(initialQuery) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.cardBg)
                .border(1.dp, customColors.cardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "🎬 SEARCH ON IMDB",
                color = Color(0xFFF59E0B),
                fontSize = headerFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Enter actor, director, movie, or celebrity name to look up directly on IMDb:",
                color = customColors.textMain,
                fontSize = baseFontSize,
                lineHeight = 16.sp
            )

            OutlinedTextField(
                value = actorQuery,
                onValueChange = { actorQuery = it },
                placeholder = { Text("e.g. Cillian Murphy, Inception", color = customColors.textMuted, fontSize = baseFontSize) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("imdb_search_input"),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF59E0B))
                        .clickable {
                            if (actorQuery.isNotBlank()) {
                                val url = "https://www.imdb.com/find/?q=${URLEncoder.encode(actorQuery, "UTF-8")}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                                onDismiss()
                            }
                        }
                        .padding(vertical = 12.dp)
                        .testTag("submit_imdb_search_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Search IMDb",
                        color = Color.Black,
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
