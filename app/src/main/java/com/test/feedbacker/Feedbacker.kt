package com.test.feedbacker

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.SensorManager
import android.net.Uri
import android.util.Log
import com.squareup.seismic.ShakeDetector
import java.io.File

class Feedbacker(val context: Context) : ShakeDetector.Listener, DialogInterface.OnClickListener {

    private var shakeDetector: ShakeDetector = ShakeDetector(this)
    private var activity: Activity? = null

    companion object {

        const val SCREENSHOT_DIRECTORY = "/screenshots";

        fun with(application: Application): Feedbacker {
            val feedbacker = Feedbacker(application.applicationContext)
            val lifecycleCallbacks = LifecycleCallbacks(feedbacker)
            application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
            return feedbacker
        }
    }

    override fun hearShake() {
        Log.d("####", "Device Shook")
        AlertDialog.Builder(activity)
            .setTitle("You shook your device. Want to share feedback ?")
            .setPositiveButton("Share", this)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setCancelable(true)
            .show()
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
        Log.d("####", "ShakeDetector Stopped...")
    }

    fun setCurrentActivity(activity: Activity?) {
        this.activity = activity
        activity?.let {
            start()
        } ?: stop()
    }

    override fun onClick(dialog: DialogInterface?, p1: Int) {
        dialog?.dismiss()
    }

    fun startFeedback() {
        val screenshotBitmap = getScreenshotBitmap()
        val path = getScreenshotDirectory(context)
        var screenShotUri: Uri? = null
        if (screenshotBitmap != null && path != null) {
            val screenshotFile: File? = Utils.saveBitmapToDirectory(screenshotBitmap, File(path))
            screenShotUri = Uri.fromFile(screenshotFile)
        }
        Log.d("####", "screenShotUri: "+screenShotUri)
    }

    private fun getScreenshotBitmap(): Bitmap? {
        val view = activity?.window?.decorView?.rootView
        if (view?.width == 0 || view?.height == 0) {
            return null
        }
        view?.let {
            val bitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            it.draw(canvas)
            return bitmap
        } ?: return null
    }

    private fun getScreenshotDirectory(context: Context): String? {
        context.filesDir?.let {
            return it.absolutePath + SCREENSHOT_DIRECTORY
        }
        return null
    }
}
