package com.bandhu.myapplication.application


import com.bandhu.myapplication.BuildConfig

import android.app.Application
import timber.log.Timber

class MainApplication:Application() {

    val TAG = "Test"
    override fun onCreate() {
        super.onCreate()
        initTimber()

    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format(
                        "[L:%s] [%s : %s]",
                        element.lineNumber,
                        element.className,
                        element.methodName
                    )
                }
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    //change this tag for logging with filter in android studio...
                    super.log(priority, TAG, "$tag >> $message", t)
                }
            })
        }
    }
}