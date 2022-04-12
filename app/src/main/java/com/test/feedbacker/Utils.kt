package com.test.feedbacker

import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object Utils {

    private const val TAG = "Utils"
    private const val BITMAP_PREFIX = "feedback"
    private const val FILE_NAME_TEMPLATE = "%s_%s.jpg"

    fun saveBitmapToDirectory(bitmap: Bitmap, directory: File): File? {
        if (!directory.mkdirs() && !directory.exists()) {
            Log.e(TAG, "Failed to create directory")
            return null
        }
        val file = File(directory, createFileName())
        var fileOutputStream: FileOutputStream? = null
        try {
            val byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream)
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(byteStream.toByteArray())
            return file
        } catch (e: IOException) {
            Log.e(TAG, e.message, e)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, e.message, e)
            }
        }
        return null
    }

    private fun createFileName(): String {
        val randomId = System.currentTimeMillis().toString()
        return String.format(
            Locale.US,
            FILE_NAME_TEMPLATE,
            BITMAP_PREFIX,
            randomId
        )
    }
}
