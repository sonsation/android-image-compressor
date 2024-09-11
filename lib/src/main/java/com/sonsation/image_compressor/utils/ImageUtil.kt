package com.sonsation.image_compressor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

object ImageUtil {

    private const val BUFFER_SIZE = 8192

    internal fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

    internal fun ByteArrayOutputStream.toBitmap(): Bitmap? {
        val byteArray = this.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    internal fun File.saveToDir(path: String): File {

        val dirFile = File(path)

        if (dirFile.isFile) {
            throw IllegalArgumentException("path is file")
        }

        if (dirFile.exists()) {
            dirFile.mkdirs()
        }

        val destFile = File(path, "${System.currentTimeMillis()}")

        return this.copyTo(destFile, true)
    }

    internal fun Bitmap.saveToDir(path: String): File {

        val dirFile = File(path)

        if (dirFile.isFile) {
            throw IllegalArgumentException("path is file")
        }

        if (dirFile.exists()) {
            dirFile.mkdirs()
        }

        val file = File(path, "${System.currentTimeMillis()}")
        val outputStream = file.outputStream()

        outputStream.use {
            it.write(toByteArray(format = Bitmap.CompressFormat.PNG, 0))
            it.flush()
        }

        return file
    }

    internal fun Uri.saveToDir(context: Context, path: String): File {

        val dirFile = File(path)

        if (dirFile.isFile) {
            throw IllegalArgumentException("path is file")
        }

        if (dirFile.exists()) {
            dirFile.mkdirs()
        }

        val inputStream = context.contentResolver.openInputStream(this) ?: throw NullPointerException("inputStream is null")
        val file = File(path, "${System.currentTimeMillis()}")

        BufferedOutputStream(file.outputStream(), BUFFER_SIZE).use { output ->
            inputStream.use { input ->
                output.write(input.readBytes())
                output.flush()
            }
        }

        return file
    }

    internal fun File.toBitmap(options: Options = Options()): Bitmap {
        return BitmapFactory.decodeFile(absolutePath, options)
    }

    internal fun File.toNullableBitmap(options: Options = Options()): Bitmap? {
        return BitmapFactory.decodeFile(absolutePath, options)
    }

    fun Bitmap.toByteArray(format: Bitmap.CompressFormat, quality: Int): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        byteArrayOutputStream.use {
            compress(format, quality, it)
        }
        return byteArrayOutputStream.toByteArray()
    }

    internal fun ByteArray.write(file: File): File {
        return file.apply {
            BufferedOutputStream(file.outputStream(), BUFFER_SIZE).use {
                it.write(this@write)
            }
        }
    }

    internal fun File.toInputStream(): ByteArrayInputStream {
        return ByteArrayInputStream(this.readBytes())
    }

    private fun ExifInterface.getMatrix(): Matrix = Matrix().apply {
        val orientation =
            getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
        }
    }

    internal fun File.toSampledBitmap(reqWidth: Int, reqHeight: Int): Bitmap {
        return Options().run {
            inJustDecodeBounds = true
            toNullableBitmap(this)
            inSampleSize = sampleSize(reqWidth, reqHeight)
            inJustDecodeBounds = false
            toBitmap(this)
        }
    }

    internal fun File.toSampledBitmap(scale: Float): Bitmap {
        return Options().run {
            inJustDecodeBounds = true
            toNullableBitmap(this)

            val reqWidth = (outWidth * scale).toInt()
            val reqHeight = (outHeight * scale).toInt()

            inSampleSize = sampleSize(reqWidth, reqHeight)
            inJustDecodeBounds = false
            toBitmap(this)
        }
    }

    private fun Options.sampleSize(reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (outHeight > reqHeight || outWidth > reqWidth) {
            val halfHeight = outHeight / 2f
            val halfWidth = outWidth / 2f
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    internal fun File.toDetermineRotation(bitmap: Bitmap): Bitmap {

        val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ExifInterface(this)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ExifInterface(this.toInputStream())
        } else {
            ExifInterface(this.absolutePath)
        }

        return bitmap.use {
            Bitmap.createBitmap(
                it,
                0,
                0,
                it.width,
                it.height,
                exif.getMatrix(),
                true
            )
        }
    }

    inline fun <T> Bitmap.use(block: (Bitmap) -> T): T {
        var newBitmap: T? = null
        try {
            newBitmap = block(this)
            return newBitmap
        } finally {
            if (this != newBitmap) {
                recycle()
            }
        }
    }

    inline fun <T> File.use(block: (File) -> T): T {
        try {
            return block(this)
        } finally {
            if (exists()) delete()
        }
    }

    internal fun File.getImageFormat(context: Context): Bitmap.CompressFormat {
        val mimeType = context.contentResolver.getType(this.toUri())
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = mimeTypeMap.getExtensionFromMimeType(mimeType)
        return when (extension) {
            "png" -> Bitmap.CompressFormat.PNG
            "webp" -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.JPEG
        }
    }
}