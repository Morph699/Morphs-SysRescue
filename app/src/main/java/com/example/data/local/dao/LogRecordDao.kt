package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.LogRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogRecordDao {
    @Query("SELECT * FROM log_records ORDER BY timestamp ASC")
    fun getAllLogs(): Flow<List<LogRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogRecordEntity): Long

    @Query("DELETE FROM log_records")
    suspend fun clearLogs()
}
