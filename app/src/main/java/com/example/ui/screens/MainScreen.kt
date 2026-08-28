package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.core.content.ContextCompat
import com.example.data.model.AppTier
import com.example.data.model.ScanMode
import com.example.service.AnalysisResult
import com.example.ui.components.BottomControls
import com.example.ui.components.CameraViewfinder
import com.example.ui.components.HeaderBanner
import com.example.ui.dialogs.AboutSupportDialog
import com.example.ui.dialogs.AuthenticityDialog
import com.example.ui.dialogs.BatchQueueDialog
import com.example.ui.dialogs.EulaDialog
import com.example.ui.dialogs.HistoryLogDialog
import com.example.ui.dialogs.ImdbSearchDialog
import com.example.ui.dialogs.ProUnlockDialog
import com.example.ui.dialogs.ReimagineDialog
import com.example.ui.dialogs.ReimagineResultDialog
import com.example.ui.dialogs.ScanResultDialog
import com.example.ui.dialogs.SystemAlertDialog
import com.example.ui.dialogs.ThemeSettingsDialog
import com.example.ui.dialogs.ValuationDialog
import com.example.ui.theme.LocalCustomColors
import com.example.ui.theme.LocalFontScale
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val customColors = LocalCustomColors.current
    val fontScale = LocalFontScale.current

    val activeTier by viewModel.activeTier.collectAsState()
    val activeLicenseKey by viewModel.activeLicenseKey.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentFontScale by viewModel.currentFontScale.collectAsState()
    val currentScanMode by viewModel.currentScanMode.collectAsState()
    val isEulaAccepted by viewModel.isEulaAccepted.collectAsState()

    val allScans by viewModel.allScans.collectAsState()
    val batchQueue by viewModel.batchQueue.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val currentZoom by viewModel.currentZoom.collectAsState()
    val isTorchOn by viewModel.isTorchOn.collectAsState()
    val isFrontCamera by viewModel.isFrontCamera.collectAsState()

    // Dialogs
    val showEula by viewModel.showEulaDialog.collectAsState()
    val showTheme by viewModel.showThemeDialog.collectAsState()
    val showPro by viewModel.showProDialog.collectAsState()
    val showHistory by viewModel.showHistoryDialog.collectAsState()
    val showBatchQueue by viewModel.showBatchQueueDialog.collectAsState()
    val showAbout by viewModel.showAboutDialog.collectAsState()
    val showContact by viewModel.showContactDialog.collectAsState()
    val showImdb by viewModel.showImdbDialog.collectAsState()
    val showScanResult by viewModel.showScanResultDialog.collectAsState()
    val showAuthenticity by viewModel.showAuthenticityDialog.collectAsState()
    val showValuation by viewModel.showValuationDialog.collectAsState()
    val showReimagine by viewModel.showReimagineDialog.collectAsState()
    val showReimagineResult by viewModel.showReimagineResultDialog.collectAsState()
    val systemAlert by viewModel.systemAlert.collectAsState()

    val currentAnalysis by viewModel.currentAnalysisResult.collectAsState()
    val currentBitmap by viewModel.currentScannedBitmap.collectAsState()
    val currentReimagineResult by viewModel.currentReimagineResult.collectAsState()
    val isGeneratingReimagine by viewModel.isGeneratingReimagine.collectAsState()

    var imdbInitialQuery by remember { mutableStateOf("") }

    // Camera runtime permission handling
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var takePictureAction by remember { mutableStateOf<((onCaptured: (Bitmap) -> Unit) -> Unit)?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = customColors.bg
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(customColors.bg)
        ) {
            // Camera Viewfinder Background Layer
            if (hasCameraPermission) {
                CameraViewfinder(
                    scanMode = currentScanMode,
                    zoomFactor = currentZoom,
                    isTorchOn = isTorchOn,
                    isFrontCamera = isFrontCamera,
                    onZoomChanged = viewModel::onZoomChanged,
                    onCaptureReady = { action ->
                        takePictureAction = action
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(customColors.bg)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Camera Permission Required",
                            color = customColors.accent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "To scan objects, products, apparel, and documents, please grant camera access.",
                            color = customColors.textMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0284C7))
                                .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Grant Permission",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Top Header Banner with Mode Selector & Nav
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .align(Alignment.TopCenter)
            ) {
                HeaderBanner(
                    currentMode = currentScanMode,
                    activeTier = activeTier,
                    onModeSelected = viewModel::switchScanMode,
                    onOpenHistory = { viewModel.showHistoryDialog.value = true },
                    onOpenTheme = { viewModel.showThemeDialog.value = true },
                    onOpenEula = { viewModel.showEulaDialog.value = true },
                    onOpenAbout = { viewModel.showAboutDialog.value = true },
                    onOpenSupport = { viewModel.showContactDialog.value = true },
                    onOpenImdb = {
                        imdbInitialQuery = ""
                        viewModel.showImdbDialog.value = true
                    },
                    onOpenPro = { viewModel.showProDialog.value = true }
                )

                // Processing or Status Floating Indicator
                AnimatedVisibility(
                    visible = isProcessing,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xCC000000))
                            .border(1.dp, customColors.highlight, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(2.dp),
                            strokeWidth = 2.dp,
                            color = customColors.highlight
                        )
                        Text(
                            text = statusMessage ?: "Analyzing visual vectors...",
                            color = customColors.highlight,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Shutter & Zoom HUD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .align(Alignment.BottomCenter)
            ) {
                BottomControls(
                    currentMode = currentScanMode,
                    batchQueue = batchQueue,
                    zoomFactor = currentZoom,
                    isTorchOn = isTorchOn,
                    isProcessing = isProcessing,
                    onZoomChanged = viewModel::onZoomChanged,
                    onShutterClick = {
                        takePictureAction?.invoke { capturedBitmap ->
                            viewModel.onCaptureShutter(capturedBitmap)
                        }
                    },
                    onToggleTorch = viewModel::toggleTorch,
                    onToggleCameraLens = viewModel::toggleCameraLens,
                    onOpenBatchQueue = { viewModel.showBatchQueueDialog.value = true },
                    onGalleryImageSelected = { uri ->
                        try {
                            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)) { decoder, _, _ ->
                                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                                    decoder.isMutableRequired = true
                                }
                            } else {
                                @Suppress("DEPRECATION")
                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                            }
                            viewModel.onCaptureShutter(bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                )
            }

            // DIALOG OVERLAYS
            if (showEula) {
                EulaDialog(
                    onAgree = { viewModel.acceptEula() },
                    onDismiss = if (isEulaAccepted) ({ viewModel.showEulaDialog.value = false }) else null
                )
            }

            if (showScanResult && currentAnalysis != null) {
                ScanResultDialog(
                    result = currentAnalysis!!,
                    scannedBitmap = currentBitmap,
                    onDismiss = { viewModel.showScanResultDialog.value = false },
                    onOpenImdb = { query ->
                        imdbInitialQuery = query
                        viewModel.showScanResultDialog.value = false
                        viewModel.showImdbDialog.value = true
                    }
                )
            }

            if (showAuthenticity && currentAnalysis != null) {
                AuthenticityDialog(
                    result = currentAnalysis!!,
                    onDismiss = { viewModel.showAuthenticityDialog.value = false }
                )
            }

            if (showValuation && currentAnalysis != null) {
                ValuationDialog(
                    result = currentAnalysis!!,
                    onDismiss = { viewModel.showValuationDialog.value = false }
                )
            }

            if (showReimagine) {
                ReimagineDialog(
                    capturedBitmap = currentBitmap,
                    onGenerate = { objectTag, stylePreset ->
                        viewModel.startReimagineGeneration(objectTag, stylePreset)
                    },
                    onDismiss = { viewModel.showReimagineDialog.value = false }
                )
            }

            if (showReimagineResult) {
                ReimagineResultDialog(
                    result = currentReimagineResult,
                    isLoading = isGeneratingReimagine,
                    onDismiss = { viewModel.showReimagineResultDialog.value = false }
                )
            }

            if (showBatchQueue) {
                BatchQueueDialog(
                    batchQueue = batchQueue,
                    activeTier = activeTier,
                    onInspectLens = { item ->
                        val url = "https://lens.google.com/uploadbyurl?url=${Uri.encode(item.identifiedTitle ?: "item")}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    },
                    onInspectImdb = { item ->
                        imdbInitialQuery = item.identifiedTitle ?: ""
                        viewModel.showBatchQueueDialog.value = false
                        viewModel.showImdbDialog.value = true
                    },
                    onDeleteItem = viewModel::deleteBatchItem,
                    onClearAll = viewModel::clearBatchQueue,
                    onExportCsv = { viewModel.getExportBatchCsv() },
                    onExportJson = { viewModel.getExportBatchJson() },
                    onOpenPro = {
                        viewModel.showBatchQueueDialog.value = false
                        viewModel.showProDialog.value = true
                    },
                    onDismiss = { viewModel.showBatchQueueDialog.value = false }
                )
            }

            if (showHistory) {
                HistoryLogDialog(
                    scans = allScans,
                    logs = allLogs,
                    activeTier = activeTier,
                    onDeleteScan = viewModel::deleteScan,
                    onClearAllScans = viewModel::clearAllScans,
                    onClearLogs = viewModel::clearLogs,
                    onExportCsv = { viewModel.getExportScansCsv() },
                    onExportJson = { viewModel.getExportScansJson() },
                    onOpenPro = {
                        viewModel.showHistoryDialog.value = false
                        viewModel.showProDialog.value = true
                    },
                    onDismiss = { viewModel.showHistoryDialog.value = false }
                )
            }

            if (showTheme) {
                ThemeSettingsDialog(
                    currentTheme = currentTheme,
                    currentFontScale = currentFontScale,
                    activeTier = activeTier,
                    onSelectTheme = viewModel::setTheme,
                    onSelectFontScale = viewModel::setFontScale,
                    onDismiss = { viewModel.showThemeDialog.value = false }
                )
            }

            if (showPro) {
                ProUnlockDialog(
                    activeTier = activeTier,
                    activeKey = activeLicenseKey,
                    onActivateKey = viewModel::activateLicenseKey,
                    onUnlockTierDirectly = viewModel::unlockTierDirectly,
                    onDeactivateLicense = viewModel::deactivateLicense,
                    onDismiss = { viewModel.showProDialog.value = false }
                )
            }

            if (showImdb) {
                ImdbSearchDialog(
                    initialQuery = imdbInitialQuery,
                    onDismiss = { viewModel.showImdbDialog.value = false }
                )
            }

            if (showAbout) {
                AboutSupportDialog(
                    isAboutMode = true,
                    onDismiss = { viewModel.showAboutDialog.value = false }
                )
            }

            if (showContact) {
                AboutSupportDialog(
                    isAboutMode = false,
                    onDismiss = { viewModel.showContactDialog.value = false }
                )
            }

            systemAlert?.let { (title, msg) ->
                SystemAlertDialog(
                    title = title,
                    message = msg,
                    onDismiss = { viewModel.dismissAlert() }
                )
            }
        }
    }
}
