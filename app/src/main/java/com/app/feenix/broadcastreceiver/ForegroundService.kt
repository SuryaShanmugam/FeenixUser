package com.app.feenix.broadcastreceiver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.app.feenix.app.Constant
import com.app.feenix.feature.internet.InternetConnectionServiceHandler
import com.app.feenix.notification.AlarmNotificationFactory
import com.app.feenix.notification.NotificationConstants
import com.app.feenix.utils.Log
import kotlinx.coroutines.*

class ForegroundService : Service() {

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Log.foregroundLog("on create ForegroundService")

        createForegroundService()

    }

    private fun createForegroundService() {
        val notification =
            AlarmNotificationFactory(this).executeForegroundFactory()
        startForeground(NotificationConstants.FOREGROUND_NOTIFICATION_ID, notification)
        Log.debug("fgdgdgd", "createForegroundService() called for ForegroundService")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.debug("onStartCommand()", " called for ForegroundService")
        var isServiceStart = false
        var isActivityStartedService = false
        intent?.extras?.let {
            isActivityStartedService = it.getBoolean(Constant.ACTIVITY_STARTED_SERVICE)
            isServiceStart = it.getBoolean(Constant.IS_SERVICE_START)
            Log.foregroundLog("isActivityStartedService = $isActivityStartedService; isServiceStart = $isServiceStart")
        }
        startPeriodicTimer()
        return START_STICKY
    }

    private fun startPeriodicTimer() {
        if (job == null) {
            launchTimer()
        } else {
            job?.let {
                if (!it.isActive) {
                    startInternetConnectionProcess()
                }
            }
        }
    }

    private fun launchTimer() {
        Log.debug("launchTimer", "createForegroundService() called for ForegroundService")
        job = scope.launch {
            while (isActive) {
                Log.foregroundLog("PeriodicTimer timer task called for ForegroundService")

                startInternetConnectionProcess()

            }
            delay(60000)
        }
    }


    private fun startInternetConnectionProcess() {
        Log.debug(
            "startInternetConnectionProcess",
            "createForegroundService() called for ForegroundService"
        )
        InternetConnectionServiceHandler.getInstance().initProcess(this@ForegroundService)
    }

    override fun onDestroy() {
        Log.foregroundLog("onDestroy() called for ForegroundService")
        try {
            job?.cancel()
            job = null
            scope.cancel()
        } catch (ex: Exception) {
            Log.foregroundLog("job & scope cancelled exception = ${ex.message}")
        }
        super.onDestroy()
    }

}

