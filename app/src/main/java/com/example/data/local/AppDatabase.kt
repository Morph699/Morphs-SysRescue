package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BatchItemDao
import com.example.data.local.dao.LogRecordDao
import com.example.data.local.dao.ScanRecordDao
import com.example.data.local.entity.BatchItemEntity
import com.example.data.local.entity.LogRecordEntity
import com.example.data.local.entity.ScanRecordEntity

@Database(
    entities = [
        ScanRecordEntity::class,
        BatchItemEntity::class,
        LogRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanRecordDao(): ScanRecordDao
    abstract fun batchItemDao(): BatchItemDao
    abstract fun logRecordDao(): LogRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "morphs_image_identifier.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
