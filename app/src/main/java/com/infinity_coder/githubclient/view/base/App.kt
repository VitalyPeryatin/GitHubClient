package com.infinity_coder.githubclient.view.base

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}