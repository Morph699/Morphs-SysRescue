package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageUtils {

    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, prefix: String = "scan"): File {
        val storageDir = File(context.filesDir, "scans")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val file = File(storageDir, "${prefix}_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file
    }

    fun cropWithZoom(source: Bitmap, zoomFactor: Float): Bitmap {
        if (zoomFactor <= 1.05f) return source
        val originalWidth = source.width
        val originalHeight = source.height

        val cropWidth = (originalWidth / zoomFactor).toInt().coerceAtLeast(1)
        val cropHeight = (originalHeight / zoomFactor).toInt().coerceAtLeast(1)

        val x = ((originalWidth - cropWidth) / 2).coerceIn(0, originalWidth - cropWidth)
        val y = ((originalHeight - cropHeight) / 2).coerceIn(0, originalHeight - cropHeight)

        return Bitmap.createBitmap(source, x, y, cropWidth, cropHeight)
    }

    fun resizeBitmap(bitmap: Bitmap, maxDimension: Int = 1024): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxDimension && height <= maxDimension) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val targetWidth: Int
        val targetHeight: Int

        if (width > height) {
            targetWidth = maxDimension
            targetHeight = (maxDimension / ratio).toInt()
        } else {
            targetHeight = maxDimension
            targetWidth = (maxDimension * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 85): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String = "Morphs_AI"): Uri? {
        val filename = "${title}_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MorphsCreations")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        try {
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri)
                fos?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            fos?.close()
        }
        return imageUri
    }

    fun createShareIntent(context: Context, imageFile: File, title: String, text: String): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
