package com.app.feenix.utils

import android.annotation.SuppressLint
import android.provider.Settings
import com.app.feenix.R
import com.app.feenix.app.AppController
import com.app.feenix.app.ErrorBody
import com.app.feenix.webservices.NetworkConstants


object AppSnippet {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            AppController.applicationInstance.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun createUploadPushNotificationErrorBody() =
        ErrorBody(
            NetworkConstants.GENERAL_ERROR_CODE,
            LocaleContextWrapper.getLocaleString(R.string.push_notification_error)
        )
}