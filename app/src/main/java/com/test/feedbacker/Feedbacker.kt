package com.test.feedbacker

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.hardware.SensorManager
import android.util.Log
import com.squareup.seismic.ShakeDetector

class Feedbacker(val context: Context) : ShakeDetector.Listener, DialogInterface.OnClickListener {

    private var shakeDetector: ShakeDetector = ShakeDetector(this)
    private var activity: Activity? = null

    companion object {
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
        // TODO
    }
}
