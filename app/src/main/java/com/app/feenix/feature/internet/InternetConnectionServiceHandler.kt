package com.app.feenix.feature.internet

import android.content.Context
import android.os.Bundle
import com.app.feenix.app.AppController
import com.app.feenix.app.Constant
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager


class InternetConnectionServiceHandler {

    companion object {
        // Singleton prevents multiple instances opening at the
        // same time.
        @Volatile
        private var instance: InternetConnectionServiceHandler? = null
        private val LOCK = Any()

        fun getInstance() = instance ?: init()

        private fun init(): InternetConnectionServiceHandler {
            synchronized(LOCK) {
                val tempInstance = InternetConnectionServiceHandler()
                instance = tempInstance
                return tempInstance
            }
        }
    }

    fun initProcess(context: Context) {
        checkInternetConnection(context)

    }

    private fun saveAndUpdate(networkAvailable: Boolean) {

        processConnectionStatus(
            InternetConnectionManager.getInstance().getCurrentSignalStrength(),
            networkAvailable
        )
    }

    private fun checkInternetConnection(context: Context) {
        val isNetworkAvailable =
            InternetConnectionManager.getInstance().checkInternetConnectionWithHost(context)
        saveAndUpdate(isNetworkAvailable)
    }

    private fun processConnectionStatus(connectionStatus: Int, networkAvailable: Boolean) {
        sendConnectionStatusBroadcast(connectionStatus)
    }


    private fun sendConnectionStatusBroadcast(connectionStatus: Int) {
        val bundle = Bundle()
        bundle.putInt(Constant.INTERNET_CONNECTION_STATUS, connectionStatus)
        ServicesBroadcastManager(AppController.applicationInstance).triggerInternetConnectionBroadcast(
            bundle
        )
    }
}

