package com.test.feedbacker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import android.util.Log
import com.squareup.seismic.ShakeDetector

class Feedbacker(val context: Context) : ShakeDetector.Listener {

    var shakeDetector: ShakeDetector = ShakeDetector(this)
    var act: Activity? = null

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
    }

    private fun start() {
        shakeDetector.start(
            act?.getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            SensorManager.SENSOR_DELAY_GAME
        )
        Log.d("####", "ShakeDetector Started...")
    }

    private fun stop() {
        shakeDetector.stop()
        Log.d("####", "ShakeDetector Stopped...")
    }

    fun setActivity(activity: Activity?) {
        this.act = activity
        activity?.let {
            start()
        } ?: stop()
    }
}
