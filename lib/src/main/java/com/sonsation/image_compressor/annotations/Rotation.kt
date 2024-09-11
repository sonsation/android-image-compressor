package com.sonsation.image_compressor.annotations

import androidx.annotation.FloatRange

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@FloatRange(0.0, 360.0)
annotation class Rotation()
