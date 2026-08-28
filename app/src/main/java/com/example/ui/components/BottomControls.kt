package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.BatchItemEntity
import com.example.data.model.ScanMode
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import java.io.File

@Composable
fun BottomControls(
    currentMode: ScanMode,
    batchQueue: List<BatchItemEntity>,
    zoomFactor: Float,
    isTorchOn: Boolean,
    isProcessing: Boolean,
    onZoomChanged: (Float) -> Unit,
    onShutterClick: () -> Unit,
    onToggleTorch: () -> Unit,
    onToggleCameraLens: () -> Unit,
    onOpenBatchQueue: () -> Unit,
    onGalleryImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    var isPressed by remember { mutableStateOf(false) }
    val shutterScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        label = "shutter_scale"
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onGalleryImageSelected(it) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Queue HUD Tray (shows when Batch Queue active or items queued)
        AnimatedVisibility(visible = currentMode == ScanMode.BATCH_QUEUE || batchQueue.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .widthIn(max = 360.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(customColors.headerBg)
                    .border(1.dp, customColors.accent, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎞️ Queue: ${batchQueue.size}",
                    color = customColors.accent,
                    fontSize = (11.5.sp * fontScale.scaleFactor),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("batch_queue_count_label")
                )

                // Mini thumbnails bar
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    batchQueue.takeLast(6).forEach { item ->
                        AsyncImage(
                            model = File(item.imagePath),
                            contentDescription = "Queued Snap",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(1.dp, customColors.accent, RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(customColors.btnBg)
                        .clickable { onOpenBatchQueue() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("review_batch_queue_btn")
                ) {
                    Text(
                        text = "👁️ Review",
                        color = Color.White,
                        fontSize = (10.5.sp * fontScale.scaleFactor),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Zoom Slider Controls
        Row(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.headerBg)
                .border(1.dp, customColors.headerBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = String.format("%.1fx", zoomFactor),
                color = customColors.accent,
                fontSize = (11.5.sp * fontScale.scaleFactor),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp)
            )

            Slider(
                value = zoomFactor,
                onValueChange = onZoomChanged,
                valueRange = 1.0f..8.0f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("camera_zoom_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = customColors.accent,
                    activeTrackColor = customColors.accent,
                    inactiveTrackColor = customColors.btnBg
                )
            )
        }

        // Shutter & Camera Controls Row
        Row(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flash Toggle
            IconButton(
                onClick = onToggleTorch,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(customColors.headerBg)
                    .border(1.dp, customColors.headerBorder, CircleShape)
                    .testTag("torch_toggle_btn")
            ) {
                Icon(
                    imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Flashlight Toggle",
                    tint = if (isTorchOn) customColors.highlight else customColors.accent
                )
            }

            // Central Shutter Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .scale(shutterScale)
                    .clip(CircleShape)
                    .background(Color(0x44FFFFFF))
                    .border(4.dp, Color.White, CircleShape)
                    .pointerInput(isProcessing) {
                        detectTapGestures(
                            onPress = {
                                if (!isProcessing) {
                                    isPressed = true
                                    tryAwaitRelease()
                                    isPressed = false
                                    onShutterClick()
                                }
                            }
                        )
                    }
                    .testTag("camera_shutter_button"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isProcessing) customColors.highlight else Color.White)
                )
            }

            // Lens Switcher (Back / Front)
            IconButton(
                onClick = onToggleCameraLens,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(customColors.headerBg)
                    .border(1.dp, customColors.headerBorder, CircleShape)
                    .testTag("switch_lens_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch Camera Lens",
                    tint = customColors.accent
                )
            }

            // Import Photo From Gallery
            IconButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(customColors.headerBg)
                    .border(1.dp, customColors.headerBorder, CircleShape)
                    .testTag("import_gallery_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Pick Image from Gallery",
                    tint = customColors.accent
                )
            }
        }
    }
}
