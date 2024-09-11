package com.sonsation.image_compressor.annotations

import androidx.annotation.FloatRange

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@FloatRange(from = 0.0, to = 1.0)
annotation class Scale()
