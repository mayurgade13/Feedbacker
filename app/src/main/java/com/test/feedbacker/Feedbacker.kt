package com.test.feedbacker

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.SensorManager
import android.net.Uri
import android.util.Log
import com.jraska.falcon.Falcon
import com.squareup.seismic.ShakeDetector
import java.io.File

class Feedbacker(private val context: Context) : ShakeDetector.Listener {

    private var shakeDetector: ShakeDetector = ShakeDetector(this)
    private var activity: Activity? = null
    private var isFeedbackFlowStarted = false

    companion object {
        const val SCREENSHOT_DIRECTORY = "/screenshots"

        fun with(application: Application): Feedbacker {
            val feedbacker = Feedbacker(application.applicationContext)
            val lifecycleCallbacks = LifecycleCallbacks(feedbacker)
            application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
            return feedbacker
        }
    }

    override fun hearShake() {
        Log.d("####", "Device Shook")
        if (!isFeedbackFlowStarted) {
            isFeedbackFlowStarted = true
            AlertDialog.Builder(activity)
                .setTitle("You shook your device. Want to share feedback ?")
                .setPositiveButton("Share") { dialog, _ ->
                    dialog?.dismiss()
                    isFeedbackFlowStarted = false
                    startFeedback()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog?.cancel()
                    isFeedbackFlowStarted = false
                }
                .setCancelable(true)
                .show()
        }
    }

    private fun start() {
        shakeDetector.start(
            activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            SensorManager.SENSOR_DELAY_UI
        )
        Log.d("####", "ShakeDetector Started...")
    }

    private fun stop() {
        shakeDetector.stop()
        isFeedbackFlowStarted = false
        Log.d("####", "ShakeDetector Stopped...")
    }

    fun setCurrentActivity(activity: Activity?) {
        this.activity = activity
        activity?.let {
            start()
        } ?: stop()
    }

    fun startFeedback() {
        val screenshotBitmap = getScreenshotBitmap()
        val path = getScreenshotDirectory(context)
        var screenShotUri: Uri? = null
        if (screenshotBitmap != null && path != null) {
            val screenshotFile: File? = Utils.saveBitmapToDirectory(screenshotBitmap, File(path))
            screenShotUri = Uri.fromFile(screenshotFile)
            Log.d("####", "screenShotUri: $screenShotUri")
            showScreenshotPreview(screenShotUri)
            isFeedbackFlowStarted = false
        } else {
            Log.d("####", "Something went wrong: bitmap - $screenshotBitmap")
            isFeedbackFlowStarted = false
        }
    }

    private fun getScreenshotBitmap(): Bitmap? {
        try {
            // Falcon can take screenshot of alerts, modals, toasts
            return Falcon.takeScreenshotBitmap(activity)
        } catch (exception: Falcon.UnableToTakeScreenshotException) {
            val view = activity?.window?.decorView?.rootView
            if (view?.width == 0 || view?.height == 0) {
                return null
            }
            view?.let {
                val bitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.RGB_565)
                val canvas = Canvas(bitmap)
                it.draw(canvas)
                return bitmap
            }
        }
        return null
    }

    private fun getScreenshotDirectory(context: Context): String? {
        context.filesDir?.let {
            return it.absolutePath + SCREENSHOT_DIRECTORY
        }
        return null
    }

    private fun showScreenshotPreview(screenShotUri: Uri?) {
        val intent = Intent(activity, ScreenshotPreviewActivity::class.java)
        intent.putExtra("URI", screenShotUri)
        activity?.startActivity(intent)
    }
}
