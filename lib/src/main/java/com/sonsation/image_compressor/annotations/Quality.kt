package com.sonsation.image_compressor.annotations

import androidx.annotation.IntRange

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@IntRange(0, 100)
annotation class Quality()
