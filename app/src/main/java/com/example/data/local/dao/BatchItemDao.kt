package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BatchItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchItemDao {
    @Query("SELECT * FROM batch_items ORDER BY timestamp ASC")
    fun getAllBatchItems(): Flow<List<BatchItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatchItem(item: BatchItemEntity): Long

    @Update
    suspend fun updateBatchItem(item: BatchItemEntity)

    @Query("DELETE FROM batch_items WHERE id = :id")
    suspend fun deleteBatchItemById(id: Long)

    @Query("DELETE FROM batch_items")
    suspend fun clearBatchQueue()

    @Query("SELECT COUNT(*) FROM batch_items")
    fun getBatchCount(): Flow<Int>
}
