package com.openclassroom.vitesse.data

import android.content.Context
import android.graphics.Bitmap

interface BitmapLoader {
    fun loadBitmap(photoUriString: String, context: Context): Bitmap?
}