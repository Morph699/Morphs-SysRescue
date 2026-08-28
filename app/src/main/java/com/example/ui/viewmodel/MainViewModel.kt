package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.local.entity.BatchItemEntity
import com.example.data.local.entity.LogRecordEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.model.AppTier
import com.example.data.model.FontScaleLevel
import com.example.data.model.LicenseKeyValidator
import com.example.data.model.ScanMode
import com.example.data.model.ThemeType
import com.example.data.repository.ImageIdentifierRepository
import com.example.service.AnalysisResult
import com.example.service.ReimagineResult
import com.example.service.VisualAnalysisService
import com.example.util.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferencesRepository(application)
    private val repository = ImageIdentifierRepository(application)
    private val visualService = VisualAnalysisService()

    val isEulaAccepted: StateFlow<Boolean> = userPrefs.isEulaAccepted
    val activeTier: StateFlow<AppTier> = userPrefs.activeTier
    val activeLicenseKey: StateFlow<String?> = userPrefs.activeLicenseKey
    val currentTheme: StateFlow<ThemeType> = userPrefs.currentTheme
    val currentFontScale: StateFlow<FontScaleLevel> = userPrefs.currentFontScale
    val currentScanMode: StateFlow<ScanMode> = userPrefs.currentScanMode

    val allScans: StateFlow<List<ScanRecordEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val batchQueue: StateFlow<List<BatchItemEntity>> = repository.batchQueue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLogs: StateFlow<List<LogRecordEntity>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>("Ready for visual scan")
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _currentZoom = MutableStateFlow(1.0f)
    val currentZoom: StateFlow<Float> = _currentZoom.asStateFlow()

    private val _isTorchOn = MutableStateFlow(false)
    val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

    private val _isFrontCamera = MutableStateFlow(false)
    val isFrontCamera: StateFlow<Boolean> = _isFrontCamera.asStateFlow()

    // Dialog state management
    val showEulaDialog = MutableStateFlow(false)
    val showThemeDialog = MutableStateFlow(false)
    val showProDialog = MutableStateFlow(false)
    val showHistoryDialog = MutableStateFlow(false)
    val showBatchQueueDialog = MutableStateFlow(false)
    val showAboutDialog = MutableStateFlow(false)
    val showContactDialog = MutableStateFlow(false)
    val showImdbDialog = MutableStateFlow(false)
    val showScanResultDialog = MutableStateFlow(false)
    val showAuthenticityDialog = MutableStateFlow(false)
    val showValuationDialog = MutableStateFlow(false)
    val showReimagineDialog = MutableStateFlow(false)
    val showReimagineResultDialog = MutableStateFlow(false)
    val systemAlert = MutableStateFlow<Pair<String, String>?>(null)

    val currentAnalysisResult = MutableStateFlow<AnalysisResult?>(null)
    val currentScannedBitmap = MutableStateFlow<Bitmap?>(null)
    val currentScannedFileUri = MutableStateFlow<Uri?>(null)
    val currentReimagineResult = MutableStateFlow<ReimagineResult?>(null)
    val isGeneratingReimagine = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.addLog("System initialized. Native engine ready.")
            if (!userPrefs.isEulaAccepted.value) {
                showEulaDialog.value = true
            }
        }
    }

    fun onZoomChanged(zoom: Float) {
        _currentZoom.value = zoom.coerceIn(1.0f, 8.0f)
    }

    fun toggleTorch() {
        _isTorchOn.value = !_isTorchOn.value
    }

    fun toggleCameraLens() {
        _isFrontCamera.value = !_isFrontCamera.value
    }

    fun acceptEula() {
        userPrefs.setEulaAccepted(true)
        showEulaDialog.value = false
        viewModelScope.launch {
            repository.addLog("Terms of service accepted by user.")
        }
    }

    fun switchScanMode(mode: ScanMode) {
        val tier = activeTier.value
        if (mode.requiredTier.level > tier.level) {
            showProDialog.value = true
            showAlert("Mode Locked", "${mode.displayName} requires ${mode.requiredTier.title} upgrade or valid license key.")
            return
        }
        userPrefs.setScanMode(mode)
        viewModelScope.launch {
            repository.addLog("Switched mode to: ${mode.displayName}")
        }
    }

    fun onCaptureShutter(bitmap: Bitmap) {
        if (_isProcessing.value) return
        _isProcessing.value = true
        _statusMessage.value = "Analyzing frame..."

        viewModelScope.launch {
            try {
                val croppedBitmap = ImageUtils.cropWithZoom(bitmap, _currentZoom.value)
                val savedFile = ImageUtils.saveBitmapToInternalStorage(getApplication(), croppedBitmap)
                val fileUri = Uri.fromFile(savedFile)
                currentScannedBitmap.value = croppedBitmap
                currentScannedFileUri.value = fileUri

                val mode = currentScanMode.value

                when (mode) {
                    ScanMode.BATCH_QUEUE -> {
                        val tier = activeTier.value
                        if (tier.level < AppTier.PRO.level) {
                            showProDialog.value = true
                            showAlert("Pro Gated", "Batch Scan Queue requires Pro Tier ($3.50 Auto-Key) or Lifetime upgrade.")
                            _isProcessing.value = false
                            return@launch
                        }
                        repository.insertBatchItem(savedFile.absolutePath)
                        _statusMessage.value = "Frame queued!"
                        Toast.makeText(getApplication(), "🎞️ Frame added to batch queue", Toast.LENGTH_SHORT).show()
                        _isProcessing.value = false
                    }
                    ScanMode.FAKE_CHECK -> {
                        _statusMessage.value = "Auditing authenticity vectors..."
                        val analysis = visualService.analyzeImage(croppedBitmap, ScanMode.FAKE_CHECK)
                        currentAnalysisResult.value = analysis

                        repository.insertScan(
                            ScanRecordEntity(
                                mode = "fake_check",
                                title = analysis.title,
                                category = analysis.category,
                                summary = analysis.summary,
                                details = analysis.details,
                                confidenceScore = analysis.confidenceScore,
                                authenticityVerdict = analysis.authenticityVerdict,
                                valuationEstimate = analysis.valuationEstimate,
                                imagePath = savedFile.absolutePath
                            )
                        )
                        showAuthenticityDialog.value = true
                        _isProcessing.value = false
                    }
                    ScanMode.WORTH -> {
                        val tier = activeTier.value
                        if (tier.level < AppTier.LIFETIME.level) {
                            showProDialog.value = true
                            showAlert("Lifetime Exclusive", "What's It Worth? valuation mode is exclusively unlocked with Lifetime Tier ($5.00 Auto-Key).")
                            _isProcessing.value = false
                            return@launch
                        }
                        _statusMessage.value = "Calculating market appraisal..."
                        val analysis = visualService.analyzeImage(croppedBitmap, ScanMode.WORTH)
                        currentAnalysisResult.value = analysis

                        repository.insertScan(
                            ScanRecordEntity(
                                mode = "worth",
                                title = analysis.title,
                                category = analysis.category,
                                summary = analysis.summary,
                                details = analysis.details,
                                confidenceScore = analysis.confidenceScore,
                                authenticityVerdict = analysis.authenticityVerdict,
                                valuationEstimate = analysis.valuationEstimate,
                                imagePath = savedFile.absolutePath
                            )
                        )
                        showValuationDialog.value = true
                        _isProcessing.value = false
                    }
                    ScanMode.REIMAGINE -> {
                        val tier = activeTier.value
                        if (tier.level < AppTier.LIFETIME.level) {
                            showProDialog.value = true
                            showAlert("Lifetime Exclusive", "Scan to Re-Imagine 4K AI mode is exclusively unlocked with Lifetime Tier ($5.00 Auto-Key).")
                            _isProcessing.value = false
                            return@launch
                        }
                        showReimagineDialog.value = true
                        _isProcessing.value = false
                    }
                    ScanMode.VISUAL_AI -> {
                        _statusMessage.value = "Identifying object..."
                        val analysis = visualService.analyzeImage(croppedBitmap, ScanMode.VISUAL_AI)
                        currentAnalysisResult.value = analysis

                        val lensUrl = "https://lens.google.com/uploadbyurl?url=${java.net.URLEncoder.encode(analysis.searchQuery, "UTF-8")}"
                        repository.insertScan(
                            ScanRecordEntity(
                                mode = "visual_ai",
                                title = analysis.title,
                                category = analysis.category,
                                summary = analysis.summary,
                                details = analysis.details,
                                confidenceScore = analysis.confidenceScore,
                                authenticityVerdict = analysis.authenticityVerdict,
                                valuationEstimate = analysis.valuationEstimate,
                                imagePath = savedFile.absolutePath,
                                searchUrl = lensUrl
                            )
                        )
                        showScanResultDialog.value = true
                        _isProcessing.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _statusMessage.value = "Scan failed: ${e.localizedMessage}"
                _isProcessing.value = false
            }
        }
    }

    fun startReimagineGeneration(objectTag: String, stylePreset: String) {
        val tier = activeTier.value
        if (tier.level < AppTier.LIFETIME.level) {
            showProDialog.value = true
            showAlert("Lifetime Exclusive", "Scan to Re-Imagine 4K AI is exclusively unlocked with Lifetime Tier ($5.00 Auto-Key).")
            return
        }

        showReimagineDialog.value = false
        showReimagineResultDialog.value = true
        isGeneratingReimagine.value = true

        viewModelScope.launch {
            try {
                val result = visualService.generateReimagineArt(objectTag, stylePreset)
                currentReimagineResult.value = result
                repository.addLog("Generated 4K AI art: $objectTag in $stylePreset")
            } catch (e: Exception) {
                e.printStackTrace()
                showAlert("Generation Error", "Could not generate artwork: ${e.localizedMessage}")
            } finally {
                isGeneratingReimagine.value = false
            }
        }
    }

    fun activateLicenseKey(rawKey: String): Boolean {
        val tier = userPrefs.activateKey(rawKey)
        if (tier != null) {
            viewModelScope.launch {
                repository.addLog("Algorithmic ${tier.title} key validated.")
            }
            showProDialog.value = false
            showAlert("${tier.title} Activated", "👑 All features, themes, and font scaling options for ${tier.title} are permanently active.")
            return true
        } else {
            showAlert("Activation Error", "Invalid Mathematical Key.\nPlease verify the 12-character key format and try again.")
            return false
        }
    }

    fun unlockTierDirectly(tier: AppTier) {
        userPrefs.setTierDirectly(tier)
        val validKey = LicenseKeyValidator.generateValidKey(tier)
        userPrefs.activateKey(validKey)
        showProDialog.value = false
        showAlert("${tier.title} Unlocked", "👑 Successfully activated ${tier.title}!")
    }

    fun deactivateLicense() {
        userPrefs.deactivateLicense()
        showProDialog.value = false
        viewModelScope.launch {
            repository.addLog("License deactivated by user.")
        }
        showAlert("License Reset", "License cleared. The application has reverted to Free tier.")
    }

    fun setTheme(theme: ThemeType) {
        val tier = activeTier.value
        if (theme.requiredTier.level > tier.level) {
            showProDialog.value = true
            showAlert("Theme Locked", "${theme.displayName} requires ${theme.requiredTier.title} upgrade.")
            return
        }
        userPrefs.setTheme(theme)
        viewModelScope.launch {
            repository.addLog("Applied theme: ${theme.key}")
        }
    }

    fun setFontScale(scale: FontScaleLevel) {
        val tier = activeTier.value
        if (scale.requiredTier.level > tier.level) {
            showProDialog.value = true
            showAlert("Font Gated", "${scale.displayName} requires ${scale.requiredTier.title} upgrade.")
            return
        }
        userPrefs.setFontScale(scale)
        viewModelScope.launch {
            repository.addLog("Applied font scale: ${scale.key}")
        }
    }

    fun deleteScan(id: Long) = viewModelScope.launch {
        repository.deleteScan(id)
    }

    fun clearAllScans() = viewModelScope.launch {
        repository.clearAllScans()
    }

    fun deleteBatchItem(id: Long) = viewModelScope.launch {
        repository.deleteBatchItem(id)
    }

    fun clearBatchQueue() = viewModelScope.launch {
        repository.clearBatchQueue()
    }

    fun clearLogs() = viewModelScope.launch {
        repository.clearLogs()
    }

    fun showAlert(title: String, message: String) {
        systemAlert.value = Pair(title, message)
    }

    fun dismissAlert() {
        systemAlert.value = null
    }

    suspend fun getExportScansCsv(): String = repository.exportScansAsCsv(allScans.value)
    suspend fun getExportScansJson(): String = repository.exportScansAsJson(allScans.value)
    suspend fun getExportBatchCsv(): String = repository.exportBatchAsCsv(batchQueue.value)
    suspend fun getExportBatchJson(): String = repository.exportBatchAsJson(batchQueue.value)
}
