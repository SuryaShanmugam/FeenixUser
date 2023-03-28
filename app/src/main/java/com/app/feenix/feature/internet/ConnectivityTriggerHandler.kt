package com.app.feenix.feature.internet

import android.content.Context
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.utils.Log

import java.util.*


class ConnectivityTriggerHandler {

    private var timer: Timer? = null

    companion object {
        // Singleton prevents multiple instances opening at the
        // same time.
        @Volatile
        private lateinit var instance: ConnectivityTriggerHandler
        private val LOCK = Any()

        fun getInstance() =
            instance

        fun initiate() {
            synchronized(LOCK) {
                ConnectivityTriggerHandler().also {
                    instance = it
                }
            }
        }
    }

    fun initTrigger(context: Context) {
        Log.d("InternetConn", "start trigger")
        if (timer == null) {
            timer = Timer().also {
                it.schedule(object : TimerTask() {
                    override fun run() {
                        Log.d("InternetConn", "start service @ 5min")
                        ServicesBroadcastManager(context).startInternetConnectivityService()
                    }
                }, 1000 * 30, 1000 * 30)
            }
        }
    }

    fun onStopConnectivityTrigger(context: Context) {
//        Log.d("InternetConn", "start periodic service on destroy")
        timer?.let {
            it.cancel()
            timer = null
        }
        ServicesBroadcastManager(context).startPeriodicInternetConnectivityService()
    }


}