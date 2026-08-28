package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppTier
import com.example.data.model.FontScaleLevel
import com.example.data.model.LicenseKeyValidator
import com.example.data.model.ScanMode
import com.example.data.model.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("morphs_prefs", Context.MODE_PRIVATE)

    private val _isEulaAccepted = MutableStateFlow(prefs.getBoolean(KEY_EULA_ACCEPTED, false))
    val isEulaAccepted: StateFlow<Boolean> = _isEulaAccepted.asStateFlow()

    private val _activeTier = MutableStateFlow(loadTier())
    val activeTier: StateFlow<AppTier> = _activeTier.asStateFlow()

    private val _activeLicenseKey = MutableStateFlow(prefs.getString(KEY_LICENSE_KEY, ""))
    val activeLicenseKey: StateFlow<String?> = _activeLicenseKey.asStateFlow()

    private val _currentTheme = MutableStateFlow(loadTheme())
    val currentTheme: StateFlow<ThemeType> = _currentTheme.asStateFlow()

    private val _currentFontScale = MutableStateFlow(loadFontScale())
    val currentFontScale: StateFlow<FontScaleLevel> = _currentFontScale.asStateFlow()

    private val _currentScanMode = MutableStateFlow(loadScanMode())
    val currentScanMode: StateFlow<ScanMode> = _currentScanMode.asStateFlow()

    private fun loadTier(): AppTier {
        val rawKey = prefs.getString(KEY_LICENSE_KEY, null)
        val validatedTier = LicenseKeyValidator.getTier(rawKey)
        if (validatedTier != null) return validatedTier
        val savedTierName = prefs.getString(KEY_ACTIVE_TIER, AppTier.FREE.name)
        return try {
            AppTier.valueOf(savedTierName ?: AppTier.FREE.name)
        } catch (e: Exception) {
            AppTier.FREE
        }
    }

    private fun loadTheme(): ThemeType {
        val key = prefs.getString(KEY_THEME, ThemeType.DARK.key)
        return ThemeType.entries.find { it.key == key } ?: ThemeType.DARK
    }

    private fun loadFontScale(): FontScaleLevel {
        val key = prefs.getString(KEY_FONT_SCALE, FontScaleLevel.MEDIUM.key)
        return FontScaleLevel.entries.find { it.key == key } ?: FontScaleLevel.MEDIUM
    }

    private fun loadScanMode(): ScanMode {
        val id = prefs.getString(KEY_SCAN_MODE, ScanMode.VISUAL_AI.id)
        return ScanMode.entries.find { it.id == id } ?: ScanMode.VISUAL_AI
    }

    fun setEulaAccepted(accepted: Boolean) {
        prefs.edit().putBoolean(KEY_EULA_ACCEPTED, accepted).apply()
        _isEulaAccepted.value = accepted
    }

    fun activateKey(key: String): AppTier? {
        val tier = LicenseKeyValidator.getTier(key)
        if (tier != null) {
            prefs.edit()
                .putString(KEY_LICENSE_KEY, key.uppercase().trim())
                .putString(KEY_ACTIVE_TIER, tier.name)
                .apply()
            _activeLicenseKey.value = key.uppercase().trim()
            _activeTier.value = tier
            return tier
        }
        return null
    }

    fun setTierDirectly(tier: AppTier) {
        prefs.edit().putString(KEY_ACTIVE_TIER, tier.name).apply()
        _activeTier.value = tier
    }

    fun deactivateLicense() {
        prefs.edit()
            .remove(KEY_LICENSE_KEY)
            .putString(KEY_ACTIVE_TIER, AppTier.FREE.name)
            .apply()
        _activeLicenseKey.value = ""
        _activeTier.value = AppTier.FREE
        setTheme(ThemeType.DARK)
        setFontScale(FontScaleLevel.MEDIUM)
    }

    fun setTheme(theme: ThemeType) {
        prefs.edit().putString(KEY_THEME, theme.key).apply()
        _currentTheme.value = theme
    }

    fun setFontScale(scale: FontScaleLevel) {
        prefs.edit().putString(KEY_FONT_SCALE, scale.key).apply()
        _currentFontScale.value = scale
    }

    fun setScanMode(mode: ScanMode) {
        prefs.edit().putString(KEY_SCAN_MODE, mode.id).apply()
        _currentScanMode.value = mode
    }

    companion object {
        private const val KEY_EULA_ACCEPTED = "eula_accepted"
        private const val KEY_LICENSE_KEY = "license_key"
        private const val KEY_ACTIVE_TIER = "active_tier"
        private const val KEY_THEME = "app_theme"
        private const val KEY_FONT_SCALE = "app_font_scale"
        private const val KEY_SCAN_MODE = "app_scan_mode"
    }
}
