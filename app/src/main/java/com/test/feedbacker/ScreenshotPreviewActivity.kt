package com.test.feedbacker

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.test.feedbacker.databinding.ActivityScreenshotPreviewBinding

class ScreenshotPreviewActivity : AppCompatActivity() {

    var screenshotUri: Uri? = null
    private lateinit var binding: ActivityScreenshotPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenshotPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenshotUri = intent?.extras?.get("URI") as Uri
        Log.d("####", "ScreenshotPreviewActivity # screenshotUri: "+screenshotUri)

        screenshotUri?.let {
            binding.screenshotImage.setImageURI(it)
        }
    }

    fun sendMail(view: View) {
        // TODO: Email intent
    }
}
