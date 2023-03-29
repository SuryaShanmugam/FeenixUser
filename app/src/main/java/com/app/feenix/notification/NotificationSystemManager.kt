package com.app.feenix.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class NotificationSystemManager {

    private lateinit var notificationManager: NotificationManager
    private lateinit var channelData: HashMap<String, String>

    companion object {
        // Singleton prevents multiple instances opening at the
        // same time.
        @Volatile
        private lateinit var instance: NotificationSystemManager
        private val LOCK = Any()

        fun getInstance() = instance

        fun initiate(context: Context) {
            synchronized(LOCK) {
                NotificationSystemManager().also {
                    instance = it
                    it.initManager(context)
                }
            }
        }
    }

    private fun initManager(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannelData()
        channelData.keys.forEach {
            createNotificationChannel(context, it)
        }
    }

    private fun createChannelData() {
        channelData = hashMapOf(
            "default" to "Standard"
        )
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        NotificationChannel(
            channelId,
            channelData[channelId],
            NotificationManager.IMPORTANCE_HIGH
        ).also {
            it.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            it.enableLights(true)
            notificationManager.createNotificationChannel(it)
        }
    }

    fun createForegroundNotificationChannel(channelId: String, channelName: String) {
        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        ).also {
            it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            it.enableLights(true)
            notificationManager.createNotificationChannel(it)
        }
    }


}
