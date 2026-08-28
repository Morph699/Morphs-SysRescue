package com.example.data.model

object LicenseKeyValidator {
    private const val SYM = '$'
    private val FORBIDDEN = listOf('I', 'O')

    /**
     * Validates a 12-character license key against the original algorithm:
     * - Length must be 12
     * - Does not contain 'I' or 'O'
     * - c0 <= 87
     * - char at index 4 is '7'
     * - (c0 + charAt(11)) == 153
     * - charAt(1) == ((c0 + '$') % 26) + 65
     */
    fun validateKey(rawKey: String?): Boolean {
        if (rawKey == null) return false
        val key = rawKey.trim().uppercase()
        if (key.length != 12) return false
        if (FORBIDDEN.any { key.contains(it) }) return false

        val c0 = key[0].code
        if (c0 > 87) return false
        if (key[4] != '7') return false
        if ((c0 + key[11].code) != 153) return false

        val expectedC1 = ((c0 + SYM.code) % 26) + 65
        if (key[1].code != expectedC1) return false

        return true
    }

    /**
     * Extracts tier from valid key:
     * charAt(2) == 'S' -> SUPPORTER
     * charAt(2) == 'P' -> PRO
     * other -> LIFETIME
     */
    fun getTier(rawKey: String?): AppTier? {
        if (!validateKey(rawKey)) return null
        val key = rawKey!!.trim().uppercase()
        return when (key[2]) {
            'S' -> AppTier.SUPPORTER
            'P' -> AppTier.PRO
            else -> AppTier.LIFETIME
        }
    }

    /**
     * Generates a mathematically valid test key for a given tier.
     */
    fun generateValidKey(tier: AppTier): String {
        // Choose a valid c0 where c0 <= 87 and (153 - c0) is an uppercase ASCII letter without 'I' or 'O'
        // 'A'=65 -> 153 - 65 = 88 ('X') -> Valid!
        val c0Char = 'A' // 65
        val c0 = c0Char.code
        val c1Char = (((c0 + SYM.code) % 26) + 65).toChar()
        val c2Char = when (tier) {
            AppTier.SUPPORTER -> 'S'
            AppTier.PRO -> 'P'
            AppTier.LIFETIME, AppTier.FREE -> 'L'
        }
        val c3Char = '8'
        val c4Char = '7'
        val c5Char = '9'
        val c6Char = '4'
        val c7Char = '2'
        val c8Char = 'K'
        val c9Char = 'W'
        val c10Char = 'M'
        val c11Char = (153 - c0).toChar() // 88 = 'X'

        val sb = StringBuilder()
        sb.append(c0Char).append(c1Char).append(c2Char).append(c3Char)
            .append(c4Char).append(c5Char).append(c6Char).append(c7Char)
            .append(c8Char).append(c9Char).append(c10Char).append(c11Char)
        return sb.toString()
    }
}
