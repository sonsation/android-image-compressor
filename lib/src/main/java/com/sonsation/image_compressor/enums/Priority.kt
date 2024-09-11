package com.sonsation.image_compressor.enums

enum class Priority(val value: Int) {
    MAX(0),
    HIGH(1),
    MEDIUM(2),
    LOW(3),
    MIN(4),
    CUSTOM(100),
}