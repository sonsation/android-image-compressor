package com.sonsation.image_compressor.annotations

import androidx.annotation.IntRange

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@IntRange(from = 0)
annotation class Size()
