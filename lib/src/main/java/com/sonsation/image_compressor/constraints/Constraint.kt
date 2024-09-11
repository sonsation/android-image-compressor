package com.sonsation.image_compressor.constraints

import android.graphics.Bitmap
import com.sonsation.image_compressor.Compression
import com.sonsation.image_compressor.enums.Priority
import java.io.File

abstract class Constraint(val priority: Priority) {

    abstract fun apply(bitmap: Bitmap): Bitmap
}