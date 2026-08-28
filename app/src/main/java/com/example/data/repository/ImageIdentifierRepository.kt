package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.entity.BatchItemEntity
import com.example.data.local.entity.LogRecordEntity
import com.example.data.local.entity.ScanRecordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageIdentifierRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val scanDao = db.scanRecordDao()
    private val batchDao = db.batchItemDao()
    private val logDao = db.logRecordDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val allScans: Flow<List<ScanRecordEntity>> = scanDao.getAllRecords()
    val batchQueue: Flow<List<BatchItemEntity>> = batchDao.getAllBatchItems()
    val allLogs: Flow<List<LogRecordEntity>> = logDao.getAllLogs()

    suspend fun insertScan(record: ScanRecordEntity): Long = withContext(Dispatchers.IO) {
        val id = scanDao.insertRecord(record)
        addLog("Scan recorded [${record.mode}]: ${record.title}")
        id
    }

    suspend fun deleteScan(id: Long) = withContext(Dispatchers.IO) {
        scanDao.deleteRecordById(id)
        addLog("Deleted scan record #$id")
    }

    suspend fun clearAllScans() = withContext(Dispatchers.IO) {
        scanDao.deleteAllRecords()
        addLog("Cleared all scan history records")
    }

    suspend fun insertBatchItem(imagePath: String): Long = withContext(Dispatchers.IO) {
        val item = BatchItemEntity(imagePath = imagePath)
        val id = batchDao.insertBatchItem(item)
        addLog("Frame #$id queued into session memory")
        id
    }

    suspend fun markBatchItemViewed(id: Long, title: String? = null) = withContext(Dispatchers.IO) {
        val items = batchDao.getAllBatchItems()
        // We can update the item
        val current = db.runInTransaction<BatchItemEntity?> {
            // Find in batch items
            null
        }
    }

    suspend fun deleteBatchItem(id: Long) = withContext(Dispatchers.IO) {
        batchDao.deleteBatchItemById(id)
        addLog("Removed frame #$id from batch queue")
    }

    suspend fun clearBatchQueue() = withContext(Dispatchers.IO) {
        batchDao.clearBatchQueue()
        addLog("Batch queue cleared by user")
    }

    suspend fun addLog(message: String, level: String = "INFO") = withContext(Dispatchers.IO) {
        logDao.insertLog(LogRecordEntity(message = message, level = level))
    }

    suspend fun clearLogs() = withContext(Dispatchers.IO) {
        logDao.clearLogs()
        logDao.insertLog(LogRecordEntity(message = "Log console memory cleared."))
    }

    suspend fun exportScansAsCsv(scans: List<ScanRecordEntity>): String = withContext(Dispatchers.Default) {
        val sb = StringBuilder()
        sb.append("ID,Timestamp,Mode,Title,Category,Summary,Confidence,Valuation,Authenticity,SearchURL\n")
        scans.forEach { s ->
            val timeStr = dateFormat.format(Date(s.timestamp))
            val escape = { str: String? -> "\"${(str ?: "").replace("\"", "\"\"")}\"" }
            sb.append("${s.id},${escape(timeStr)},${escape(s.mode)},${escape(s.title)},${escape(s.category)},${escape(s.summary)},${s.confidenceScore}%,${escape(s.valuationEstimate)},${escape(s.authenticityVerdict)},${escape(s.searchUrl)}\n")
        }
        sb.toString()
    }

    suspend fun exportScansAsJson(scans: List<ScanRecordEntity>): String = withContext(Dispatchers.Default) {
        val jsonArray = JSONArray()
        scans.forEach { s ->
            val obj = JSONObject().apply {
                put("id", s.id)
                put("timestamp", dateFormat.format(Date(s.timestamp)))
                put("mode", s.mode)
                put("title", s.title)
                put("category", s.category)
                put("summary", s.summary)
                put("details", s.details)
                put("confidenceScore", s.confidenceScore)
                put("valuationEstimate", s.valuationEstimate)
                put("authenticityVerdict", s.authenticityVerdict)
                put("searchUrl", s.searchUrl)
            }
            jsonArray.put(obj)
        }
        jsonArray.toString(2)
    }

    suspend fun exportBatchAsCsv(items: List<BatchItemEntity>): String = withContext(Dispatchers.Default) {
        val sb = StringBuilder()
        sb.append("Index,ID,Timestamp,Status,IdentifiedTitle,ImagePath\n")
        items.forEachIndexed { index, item ->
            val timeStr = dateFormat.format(Date(item.timestamp))
            val status = if (item.isViewed) "Viewed" else "Queued Ready"
            sb.append("${index + 1},${item.id},\"$timeStr\",\"$status\",\"${item.identifiedTitle ?: ""}\",\"${item.imagePath}\"\n")
        }
        sb.toString()
    }

    suspend fun exportBatchAsJson(items: List<BatchItemEntity>): String = withContext(Dispatchers.Default) {
        val jsonArray = JSONArray()
        items.forEachIndexed { index, item ->
            val obj = JSONObject().apply {
                put("index", index + 1)
                put("id", item.id)
                put("timestamp", dateFormat.format(Date(item.timestamp)))
                put("viewed", item.isViewed)
                put("identifiedTitle", item.identifiedTitle)
                put("imagePath", item.imagePath)
            }
            jsonArray.put(obj)
        }
        jsonArray.toString(2)
    }
}
