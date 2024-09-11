package com.sonsation.image_compressor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.sonsation.image_compressor.annotations.Quality
import com.sonsation.image_compressor.annotations.Scale
import com.sonsation.image_compressor.annotations.Size
import com.sonsation.image_compressor.constraints.Constraint
import com.sonsation.image_compressor.utils.ImageUtil.toBitmap
import com.sonsation.image_compressor.utils.ImageUtil.use
import java.io.File


class ImageCompressor(private val context: Context) {

    private val compression by lazy {
        Compression(context)
    }

    fun setSaveDirectory(path: String): ImageCompressor {
        compression.saveDirectory = path
        return this
    }

    fun setSaveDirectory(file: File): ImageCompressor {
        compression.saveDirectory = file.absolutePath
        return this
    }

    fun setImage(path: String): ImageCompressor {
        compression.input = path
        return this
    }

    fun setImage(uri: Uri): ImageCompressor {
        compression.input = uri
        return this
    }

    fun setImage(file: File): ImageCompressor {
        compression.input = file
        return this
    }

    fun setImage(bitmap: Bitmap): ImageCompressor {
        compression.input = bitmap
        return this
    }

    fun setQuality(@Quality quality: Int): ImageCompressor {
        compression.quality = quality
        return this
    }

    fun setFormat(format: Bitmap.CompressFormat): ImageCompressor {
        compression.format = format
        return this
    }

    fun setMaxWidth(@Size width: Int): ImageCompressor {
        compression.maxWidth = width
        return this
    }

    fun setMaxHeight(@Size height: Int): ImageCompressor {
        compression.maxHeight = height
        return this
    }

    fun setScale(@Scale scale: Float): ImageCompressor {
        compression.scale = scale
        return this
    }

    fun setConstraints(constraint: Constraint): ImageCompressor {
        compression.constraints.add(constraint)
        return this
    }

    fun getBitmapImage(): Bitmap {
        return compression.getCompressedImage().toBitmap()
    }

    fun getFile(): File {
        return compression.getCompressedImage()
    }
}