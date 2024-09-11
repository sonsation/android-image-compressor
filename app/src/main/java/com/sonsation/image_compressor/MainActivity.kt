package com.sonsation.image_compressor

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sonsation.image_compressor.constraints.RotationConstraint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView = findViewById<ImageView>(R.id.image)

        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                val intent = result.data
                val uri = intent?.data ?: return@ActivityResultCallback

                val bitmap = ImageCompressor(this@MainActivity)
                    .setImage(uri)
                    .setScale(0.2f)
                    .setFormat(Bitmap.CompressFormat.JPEG)
                    .setQuality(40)
                    .getBitmapImage()

                imageView.setImageBitmap(bitmap)
            }
        )

        findViewById<View>(R.id.button).setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            activityResultLauncher.launch(intent)
        })
    }
}