package com.infinity_coder.githubclient.presentation.base

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class ImageStorageManager @Inject constructor(
    private val appContext: Context
) {
    fun saveImage(url: String?, fileName: String): String? {
        val file = File(appContext.getExternalFilesDir(null), "$fileName.jpg")

        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post {
            Picasso.get()
                .load(url)
                .into(getSaveImageTarget(file))
        }
        return file.absolutePath
    }

    private fun getSaveImageTarget(file: File): SaveImageTarget =
        SaveImageTarget(
            bitmapLoaded = { bitmap ->
                saveBitmapToFile(bitmap, file)
            }
        )

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        file.createNewFile()
        val fileOutputStream = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun removeImage(filePath: String?): Boolean {
        if (filePath == null) return true
        val myFile = File(filePath)
        return myFile.exists() && myFile.delete()
    }
}