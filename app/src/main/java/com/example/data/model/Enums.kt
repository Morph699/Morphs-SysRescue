package com.example.data.model

enum class ScanMode(val id: String, val displayName: String, val requiredTier: AppTier) {
    VISUAL_AI("visual_ai", "1. Visual AI Search (FREE)", AppTier.FREE),
    FAKE_CHECK("fake_check", "2. Is It Fake? Scanner (FREE)", AppTier.FREE),
    BATCH_QUEUE("batch_queue", "3. 🔒 🎬 Batch Queue (Pro+ $3.50)", AppTier.PRO),
    WORTH("worth", "4. 🔒 What's It Worth? (Lifetime $5.00)", AppTier.LIFETIME),
    REIMAGINE("reimagine", "5. 🔒 Re-Imagine 4K (Lifetime $5.00)", AppTier.LIFETIME)
}

enum class AppTier(val level: Int, val title: String) {
    FREE(0, "Free Tier"),
    SUPPORTER(1, "Supporter"),
    PRO(2, "Pro Tier"),
    LIFETIME(3, "Lifetime Unlimited")
}

enum class ThemeType(val key: String, val displayName: String, val requiredTier: AppTier) {
    DARK("dark", "1. Dark Mode (Stealth Charcoal - FREE)", AppTier.FREE),
    LIGHT("light", "2. Light Mode (Soft Slate - FREE)", AppTier.FREE),
    GAMER("gamer", "3. ⭐ Gamer Theme (Matrix Green - Supporter+)", AppTier.SUPPORTER),
    CYBER_YELLOW("cyber_yellow", "4. 🔒 Cyber Yellow (Gold & Dark - Pro+)", AppTier.PRO),
    CYBERPUNK("cyberpunk", "5. 🔒 Cyberpunk Pink (Magenta Neon - Pro+)", AppTier.PRO),
    NORDIC("nordic", "6. 🔒 Nordic Frost (Glacial Ice Blue - Pro+)", AppTier.PRO),
    DRACULA("dracula", "7. 🔒 Dracula Purple (Gothic Lavender - Lifetime)", AppTier.LIFETIME),
    SUNSET("sunset", "8. 🔒 Sunset Amber (Warm Glow - Lifetime)", AppTier.LIFETIME),
    SOLARIZED("solarized", "9. 🔒 Solarized Ocean (Marine Teal - Lifetime)", AppTier.LIFETIME)
}

enum class FontScaleLevel(val key: String, val displayName: String, val scaleFactor: Float, val requiredTier: AppTier) {
    SMALL("small", "Small (8.0pt / 9.5px - Tight Compact - FREE)", 0.85f, AppTier.FREE),
    MEDIUM("medium", "Medium (9.0pt / 11.5px - Standard Default - FREE)", 1.0f, AppTier.FREE),
    LARGE("large", "🔒 Large (11.0pt / 13.5px - Pro Tier $3.50+)", 1.15f, AppTier.PRO),
    XLARGE("xlarge", "🔒 Extra Large (12.5pt / 15.0px - Lifetime Tier $5.00)", 1.30f, AppTier.LIFETIME)
}
