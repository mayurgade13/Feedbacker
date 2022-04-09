package com.test.feedbacker

import android.app.Activity
import android.app.Application
import android.os.Bundle

class LifecycleCallbacks(val feedbacker: Feedbacker) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        // No impl
    }

    override fun onActivityStarted(p0: Activity) {
        // No impl
    }

    override fun onActivityResumed(p0: Activity) {
        feedbacker.setActivity(p0)
    }

    override fun onActivityPaused(p0: Activity) {
        feedbacker.setActivity(null)
    }

    override fun onActivityStopped(p0: Activity) {
        // No impl
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        // No impl
    }

    override fun onActivityDestroyed(p0: Activity) {
        // No impl
    }
}
