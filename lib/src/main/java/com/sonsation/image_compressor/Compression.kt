package com.sonsation.image_compressor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.sonsation.image_compressor.constraints.Constraint
import com.sonsation.image_compressor.utils.ImageUtil.getImageFormat
import com.sonsation.image_compressor.utils.ImageUtil.saveToDir
import com.sonsation.image_compressor.utils.ImageUtil.toByteArray
import com.sonsation.image_compressor.utils.ImageUtil.toDetermineRotation
import com.sonsation.image_compressor.utils.ImageUtil.toSampledBitmap
import com.sonsation.image_compressor.utils.ImageUtil.use
import com.sonsation.image_compressor.utils.ImageUtil.write
import java.io.File

class Compression(val context: Context) {

    var format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    var maxWidth = 0
    var maxHeight = 0
    var quality = 100
    var scale = 1f
    val constraints by lazy {
        mutableListOf<Constraint>()
    }
    var saveDirectory: String = context.cacheDir.absolutePath

    var input: Any? = null

    fun getCompressedImage(): File {

        val imageFile = getInputFile()

        format = imageFile.getImageFormat(context)

        val sampledBitmap = if (maxWidth != 0 || maxHeight != 0) {
            val reqWidth = maxWidth.times(scale).toInt()
            val reqHeight = maxHeight.times(scale).toInt()
            imageFile.toSampledBitmap(reqWidth, reqHeight)
        } else {
            imageFile.toSampledBitmap(scale)
        }

        var bitmap = imageFile.toDetermineRotation(sampledBitmap)

        return try {

            constraints.sortedBy { it.priority.value }.forEach { constraint ->
                bitmap = constraint.apply(bitmap)
            }

            bitmap.use {
                it.toByteArray(format, quality)
            }.write(imageFile)
        } catch (e: Exception) {
            Log.e("ImageCompressor", "Error while compressing image : ${e}")
            throw NullPointerException("Compressed image is null")
        } finally {
            constraints.clear()
        }
    }

    private fun getInputFile(): File {

        input ?: throw IllegalArgumentException("No image source provided")

        return when (input) {
            is String -> File((input as String)).saveToDir(saveDirectory)
            is File -> (input as File).saveToDir(saveDirectory)
            is Bitmap -> (input as Bitmap).saveToDir(saveDirectory)
            is Uri -> (input as Uri).saveToDir(context, saveDirectory)
            else -> throw IllegalArgumentException("Unsupported input type")
        }
    }
}