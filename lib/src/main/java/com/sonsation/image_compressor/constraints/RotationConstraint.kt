package com.sonsation.image_compressor.constraints

import android.graphics.Bitmap
import android.graphics.Matrix
import com.sonsation.image_compressor.Compression
import com.sonsation.image_compressor.annotations.Rotation
import com.sonsation.image_compressor.enums.Priority
import com.sonsation.image_compressor.utils.ImageUtil.use

class RotationConstraint(@Rotation private val degree: Float) : Constraint(Priority.MIN) {

    override fun apply(bitmap: Bitmap): Bitmap {

        val matrix = Matrix().apply {
            if (degree != 0f) {
                postRotate(degree)
            }
        }

        return bitmap.use {
            Bitmap.createBitmap(
                it,
                0,
                0,
                it.width,
                it.height,
                matrix,
                true
            )
        }
    }
}