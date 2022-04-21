package com.test.feedbacker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startManualFeedback(view: View) {
        (application as FeedbackApplication).feedbacker.startFeedback()
    }

    fun showDialog(view: View) {
        AlertDialog.Builder(this)
            .setTitle("Alert Dialog")
            .setPositiveButton("OK", null)
            .setMessage( "This is a sample alert dialog")
            .show()
    }
}
