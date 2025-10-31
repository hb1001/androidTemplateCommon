package com.template.core.common.utils

import android.util.Base64
import java.io.File
import java.io.IOException

object ImageUtils {
    fun fileToBase64DataUrl(file: File, mimeType: String = "image/png"): String? {
        return try {
            val bytes = file.readBytes()
            val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
            "data:$mimeType;base64,$base64String"
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}