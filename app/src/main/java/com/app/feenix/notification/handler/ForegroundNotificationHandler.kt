package com.app.feenix.notification.handler

import android.app.Notification
import android.app.Notification.FOREGROUND_SERVICE_IMMEDIATE
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.feenix.R
import com.app.feenix.notification.NotificationConstants
import com.app.feenix.notification.NotificationSystemManager
import com.app.feenix.view.activities.SplashActivity

class ForegroundNotificationHandler(private val context: Context) {

    fun createForegroundNotification(): Notification {
        NotificationSystemManager.getInstance()
            .createForegroundNotificationChannel(
                NotificationConstants.FOREGROUND_CHANNEL_ID,
                NotificationConstants.FOREGROUND_CHANNEL_NAME
            )
        return createForegroundNotificationBuilder().build()
    }

    private fun createForegroundNotificationBuilder(): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(
            context,
            NotificationConstants.FOREGROUND_CHANNEL_NAME
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(getPendingIntent())
            .setContentTitle("Feenix User")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.foregroundServiceBehavior = FOREGROUND_SERVICE_IMMEDIATE
        }
        return builder
    }

    private fun getPendingIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, SplashActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}