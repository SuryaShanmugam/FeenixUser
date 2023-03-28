package com.app.feenix.services

import android.content.Context
import android.os.Bundle
import androidx.work.WorkerParameters
import com.app.feenix.app.Constant
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.feature.internet.InternetConnectionManager

class InternetConnectivityService(private val context: Context, workerParams: WorkerParameters) :
    BaseWorker(context, workerParams) {

    override fun doWork(): Result {

        checkInternetConnection()
        return super.doWork()
    }


    private fun checkInternetConnection() {
        val isNetworkAvailable =
            InternetConnectionManager.getInstance().checkInternetConnectionWithHost(context)

        saveAndUpdate(isNetworkAvailable)
    }

    private fun saveAndUpdate(networkAvailable: Boolean) {

        processConnectionStatus(
            InternetConnectionManager.getInstance().getCurrentSignalStrength(),
            networkAvailable
        )
    }

    private fun processConnectionStatus(connectionStatus: Int, networkAvailable: Boolean) {
        sendConnectionStatusBroadcast(connectionStatus)

    }


    private fun sendConnectionStatusBroadcast(connectionStatus: Int) {
        val bundle = Bundle()
        bundle.putInt(Constant.INTERNET_CONNECTION_STATUS, connectionStatus)
        ServicesBroadcastManager(context).triggerInternetConnectionBroadcast(bundle)
    }
}