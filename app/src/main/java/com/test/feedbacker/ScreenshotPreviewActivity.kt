package com.test.feedbacker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.feedbacker.databinding.ActivityScreenshotPreviewBinding

class ScreenshotPreviewActivity : AppCompatActivity() {

    private var screenshotUri: Uri? = null
    private var attachment: Uri? = null
    private lateinit var binding: ActivityScreenshotPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenshotPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenshotUri = intent?.extras?.get("URI") as Uri
        Log.d("####", "ScreenshotPreviewActivity # screenshotUri: "+screenshotUri)

        screenshotUri?.let {
            binding.screenshotImage.setImageURI(it)
            attachment = Utils.getProviderUri(this, it)
        }

        binding.btnSendMail.setOnClickListener(View.OnClickListener {
            startActivity(createEmailIntent(
                to = arrayOf("mayurgade13@gmail.com"),
                "Feedback Report",
                createSubjectFromAvailableData(),
                attachment = attachment
            ))
            finish()
        })
        binding.editTextFeedback.requestFocus()
    }

    private fun createEmailIntent(
        to: Array<String>,
        subject: String,
        text: String?,
        attachment: Uri?
    ): Intent {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.action = Intent.ACTION_SEND
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, to)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        attachment?.let {
            intent.putExtra(Intent.EXTRA_STREAM, attachment)
        }
        return intent
    }

    private fun createSubjectFromAvailableData(): String {
        return StringBuilder().append(getFeedbackDetails()).append(getSystemDetails()).toString()
    }

    private fun getFeedbackDetails(): String {
        var feedbackDetails = ""
        binding.editTextFeedback.run {
            feedbackDetails = if (!text.isNullOrEmpty() && text.isNotBlank()) {
                text.toString()
            } else {
                "Not available"
            }
        }
        return StringBuilder().append("Feedback details: ").append("\n $feedbackDetails").toString()
    }

    private fun getSystemDetails(): String {
        return StringBuilder().append("\n\nDevice details:")
            .append("\nBrand: ${Build.BRAND}")
            .append("\nModel: ${Build.MODEL}")
            .append("\n Android Version: ${Build.VERSION.RELEASE}").toString()
    }
}
