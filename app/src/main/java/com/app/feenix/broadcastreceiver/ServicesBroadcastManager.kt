package com.app.feenix.broadcastreceiver

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.work.*
import com.app.feenix.app.Constant
import com.app.feenix.services.InternetConnectivityService
import com.app.feenix.utils.Log
import java.util.concurrent.TimeUnit


/**
 * This class is used to start the services and trigger broadcast events
 * */
class ServicesBroadcastManager(private val context: Context) {


    fun triggerInternetConnectionBroadcast(bundle: Bundle) {
        Intent().also { intent ->
            intent.action = ServiceBroadcastConstants.BroadcastActions.INTERNET_CONNECTION_STATUS
            intent.putExtras(bundle)
            context.sendBroadcast(intent)
        }
    }

    fun startForegroundService(extras: Bundle?) {
        val isServiceRunning = isServiceRunning(context, ForegroundService::class.java.name)
        var bundle = extras
        if (!isServiceRunning) {
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle.putBoolean(Constant.IS_SERVICE_START, true)
        }
        Intent(context, ForegroundService::class.java).also {
            bundle?.let { it1 -> it.putExtras(it1) }
            context.startForegroundService(it)
        }
    }

    @Suppress("DEPRECATION")
    private fun isServiceRunning(context: Context, serviceName: String): Boolean {
        Log.d("SocketService", "check is service running $serviceName")
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            Log.d("SocketService", "running service ${service.service.className}")
            if (serviceName == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun stopForegroundService() {
        context.stopService(Intent(context, ForegroundService::class.java))
    }

    fun stopAllServices() {
        stopForegroundService()
        WorkManager.getInstance(context).cancelAllWork()
    }

    fun startInternetConnectivityService() {
        val requestBuilder = OneTimeWorkRequestBuilder<InternetConnectivityService>()
            .addTag(ServiceBroadcastConstants.ServiceTags.INTERNET_CONNECTIVITY_SERVICE)
        WorkManager.getInstance(context).beginUniqueWork(
            ServiceBroadcastConstants.ServiceTags.INTERNET_CONNECTIVITY_SERVICE,
            ExistingWorkPolicy.REPLACE, requestBuilder.build()
        ).enqueue()

    }

    fun startPeriodicInternetConnectivityService() {
        val periodicConnectivityRequest =
            PeriodicWorkRequestBuilder<InternetConnectivityService>(15, TimeUnit.MINUTES)
                .addTag(ServiceBroadcastConstants.ServiceTags.PERIODIC_INTERNET_CONNECTIVITY_SERVICE)
                .build()
        WorkManager.getInstance(context).enqueue(periodicConnectivityRequest)

    }

}

