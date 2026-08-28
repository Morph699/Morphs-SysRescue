package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ScanRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE mode = :mode ORDER BY timestamp DESC")
    fun getRecordsByMode(mode: String): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE id = :id")
    suspend fun getRecordById(id: Long): ScanRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ScanRecordEntity): Long

    @Update
    suspend fun updateRecord(record: ScanRecordEntity)

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun deleteAllRecords()

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getRecordCount(): Flow<Int>
}
