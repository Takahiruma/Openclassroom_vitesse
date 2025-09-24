package com.openclassroom.vitesse.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.openclassroom.vitesse.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun CandidateImage(photoUriString: String, modifier: Modifier) {
    val context = LocalContext.current
    val bitmap = remember(photoUriString) {
        try {
            if (photoUriString.isNotEmpty()) {
                val uri = Uri.parse(photoUriString)
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
            e.printStackTrace()
            null
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "candidate picture",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(R.drawable.no_image_available),
            contentDescription = "No image",
            modifier = modifier,
        )
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val filename = "candidate_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}