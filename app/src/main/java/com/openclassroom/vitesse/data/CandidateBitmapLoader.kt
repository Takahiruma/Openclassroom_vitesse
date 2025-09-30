package com.openclassroom.vitesse.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileInputStream

class CandidateBitmapLoader : BitmapLoader {
    override fun loadBitmap(photoUriString: String, context: Context): Bitmap? {
        return try {
            if (photoUriString.isNotEmpty()) {
                val uri = photoUriString.toUri()
                if (uri.scheme == "file") {
                    val file = File(uri.path!!)
                    if (file.exists()) {
                        BitmapFactory.decodeStream(FileInputStream(file))
                    } else {
                        null
                    }
                } else if (uri.scheme == "content") {
                    context.contentResolver.openInputStream(uri).use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CandidateBitmapLoader", "Error loading bitmap", e)
            null
        }
    }
}