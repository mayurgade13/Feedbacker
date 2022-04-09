package com.test.feedbacker

import android.app.Application

class FeedbackApplication: Application() {

    lateinit var feedbacker: Feedbacker

    override fun onCreate() {
        super.onCreate()

        feedbacker = Feedbacker.with(this)
    }
}
