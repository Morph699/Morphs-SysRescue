package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batch_items")
data class BatchItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String,
    val isViewed: Boolean = false,
    val identifiedTitle: String? = null,
    val mode: String = "batch_queue"
)
