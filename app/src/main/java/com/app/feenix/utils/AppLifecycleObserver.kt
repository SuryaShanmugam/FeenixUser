package com.app.feenix.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

@Suppress("unused")
object AppLifecycleObserver : LifecycleObserver {

    var isAppOnForeground: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        isAppOnForeground = true
        Log.d("LifeCycle", "App is foreground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        isAppOnForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroyed() {
        isAppOnForeground = false
    }
}