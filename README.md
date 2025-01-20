[![](https://jitpack.io/v/sonsation/image-compressor.svg)](https://jitpack.io/#sonsation/image-compressor)

# Image Compressor

A lightweight and efficient image compression library for Android. It allows you to easily compress images while customizing properties like quality, scale, and resolution. It supports various input types such as `String`, `File`, `Bitmap`, and `Uri`.

Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:

```gradle
dependencies {
    compile 'com.github.sonsation:image-compressor:1.0.0'
}
```

## Features

- Compress images with customizable quality and scale
- Supports multiple image formats (JPEG, PNG, WebP)
- Resize images to a specified width and height
- Supports applying custom constraints to the compression process
- Save compressed images to a custom directory

## Installation

### Gradle Dependency

```gradle
dependencies {
    implementation 'com.sonsation:image_compressor:1.0.0'
}
```

## Usage

### Setting Up Compression

To start using the compressor, you can initialize the `ImageCompressor` class with a `Context` and set various parameters to customize the compression.

```kotlin
val compressor = ImageCompressor(context)
```

### Set Input Image

You can set the image to compress using different types of inputs:

```kotlin
compressor.setImage(path: String) // Path to the image file
compressor.setImage(uri: Uri)     // URI of the image
compressor.setImage(file: File)   // File object of the image
compressor.setImage(bitmap: Bitmap) // Bitmap object of the image
```

### Configure Compression Settings

You can configure different compression settings like quality, scale, and size:

```kotlin
compressor.setQuality(quality = 80) // Set image quality (0-100)
compressor.setScale(scale = 0.8f)  // Set scale factor for image resizing
compressor.setMaxWidth(width = 800) // Set max width for resizing
compressor.setMaxHeight(height = 600) // Set max height for resizing
compressor.setFormat(Bitmap.CompressFormat.JPEG) // Set output format (JPEG, PNG, WebP)
```

### Apply Constraints

You can apply constraints to the compression process, such as limiting the file size or applying custom transformations.

```kotlin
compressor.setConstraints(constraint) // Set a custom constraint
```

### Get Compressed Image

You can get the compressed image as a `Bitmap` or save it to a file:

```kotlin
val compressedBitmap: Bitmap = compressor.getBitmapImage()
val compressedFile: File = compressor.getFile()
```

### Save Compressed Image

You can save the compressed image to a specific directory or file path:

```kotlin
compressor.save("/path/to/save/directory")
compressor.save(File("/path/to/save/file"))
```

## Constraints

The `Constraint` class allows you to apply custom transformations and filters to the image during the compression process. Each constraint has a priority level, and constraints are applied in order of priority.

### Creating a Custom Constraint

To create your own custom constraint, extend the `Constraint` class and implement the `apply()` method to modify the `Bitmap` as needed. You can also set the priority for the constraint, which determines its order of execution.

```kotlin
class CustomConstraint(priority: Priority) : Constraint(priority) {
    override fun apply(bitmap: Bitmap): Bitmap {
        // Apply your transformation to the bitmap
        // Example: applying a filter, rotating, cropping, etc.
        return bitmap // Return the modified bitmap
    }
}
```

### Applying Constraints

Once you've defined a custom constraint, you can add it to the `ImageCompressor` using the `setConstraints()` method. Constraints will be applied in the order of their priority.

```kotlin
val compressor = ImageCompressor(context)

val customConstraint = CustomConstraint(Priority.HIGH)

compressor.setConstraints(customConstraint)
    .setImage("/path/to/image.jpg")
    .setQuality(quality = 80)
    .setFormat(Bitmap.CompressFormat.JPEG)

val compressedImage = compressor.getBitmapImage()
```

### Priority Enum

The `Priority` enum allows you to define the priority level for constraints. You can choose from different levels such as `MAX`, `HIGH`, `MEDIUM`, `LOW`, `MIN`, and `CUSTOM`.

```kotlin
enum class Priority(val value: Int) {
    MAX(0),
    HIGH(1),
    MEDIUM(2),
    LOW(3),
    MIN(4),
    CUSTOM(100),
}
```

By sorting the constraints by their priority, you can control the order in which they are applied to the image during the compression process.

## Example

Here’s a full example of how to use the `ImageCompressor` class:

```kotlin
val compressor = ImageCompressor(context)

compressor.setImage(path = "/path/to/image.jpg")
    .setQuality(quality = 75)
    .setScale(scale = 0.7f)
    .setMaxWidth(800)
    .setMaxHeight(600)
    .setFormat(Bitmap.CompressFormat.JPEG)

val compressedImage = compressor.getBitmapImage()

// Save the compressed image
compressor.save("/path/to/save/compressed_image")
```

License
```
Copyright 2024 Jong Heon Son (sonsation)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
