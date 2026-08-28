package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: String,
    val title: String,
    val category: String,
    val summary: String,
    val details: String = "",
    val confidenceScore: Int = 95,
    val valuationEstimate: String? = null,
    val authenticityVerdict: String? = null,
    val imagePath: String? = null,
    val searchUrl: String? = null,
    val isViewed: Boolean = true,
    val isFavorite: Boolean = false
)
