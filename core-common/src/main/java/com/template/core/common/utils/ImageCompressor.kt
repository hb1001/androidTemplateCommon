package com.template.core.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context,
    // 注入一个计算密集型任务的调度器
    @com.template.core.common.coroutine.DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun compress(
        file: File,
        maxSizeKb: Int = 50, // 目标最大体积，单位 KB (1MB)
        resolution: Pair<Int, Int> = 1080 to 1920 // 目标最大分辨率
    ): File = withContext(defaultDispatcher) {
        var quality = 90
        var compressedFile = file
        var currentSize = compressedFile.length() / 1024

        // 1. 初步压缩，如果文件已经够小，直接返回
        if (currentSize <= maxSizeKb) {
            Timber.d("  (${currentSize}KB). No compression needed.")
            return@withContext compressedFile
        }

        // 2. 缩放分辨率
        val scaledBitmap = decodeAndScaleBitmap(file, resolution.first, resolution.second)
        // 旋转图片以修正方向
        val rotatedBitmap = rotateBitmapIfRequired(file, scaledBitmap)

        // 3. 质量压缩循环
        try {
            var fos: FileOutputStream? = null
            // 创建一个新的临时文件来保存压缩后的图片
            val tempFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)

            while (currentSize > maxSizeKb && quality > 20) {
                fos?.close()
                fos = FileOutputStream(tempFile)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
                currentSize = tempFile.length() / 1024
                Timber.d("Compressing with quality $quality, size: ${currentSize}KB")
                quality -= 10 // 每次降低 10% 的质量
            }
            fos?.close()
            compressedFile = tempFile
        } catch (e: IOException) {
            Timber.e(e, "Image compression failed.")
            // 如果失败，返回原文件
            return@withContext file
        } finally {
            scaledBitmap.recycle()
            rotatedBitmap.recycle()
        }

        Timber.d("Compression finished. Final size: ${compressedFile.length() / 1024}KB")
        return@withContext compressedFile
    }

    private fun decodeAndScaleBitmap(file: File, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(file.absolutePath, this)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun rotateBitmapIfRequired(file: File, bitmap: Bitmap): Bitmap {
        val ei = ExifInterface(file.inputStream())
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}