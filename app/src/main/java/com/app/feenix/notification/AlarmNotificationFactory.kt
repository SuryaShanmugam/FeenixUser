package com.app.feenix.notification

import android.app.Notification
import android.content.Context
import com.app.feenix.notification.handler.ForegroundNotificationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class AlarmNotificationFactory(private val context: Context) : CoroutineScope by MainScope() {

    /**
     * Checks the notification type to initiate respective actions
     * */


    fun executeForegroundFactory(): Notification {
        return ForegroundNotificationHandler(
            context
        ).createForegroundNotification()
    }

    fun removeBeaconNotification(notificationId: Int) {
    }
}