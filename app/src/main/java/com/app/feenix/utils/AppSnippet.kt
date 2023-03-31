package com.app.feenix.utils

import android.annotation.SuppressLint
import android.provider.Settings
import com.app.feenix.app.AppController


object AppSnippet {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            AppController.applicationInstance.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


}