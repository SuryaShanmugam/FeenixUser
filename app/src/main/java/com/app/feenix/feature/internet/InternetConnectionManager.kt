package com.app.feenix.feature.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.app.feenix.app.AppController
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.utils.Log
import com.app.feenix.webservices.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class InternetConnectionManager : ConnectivityManager.NetworkCallback(),
    CoroutineScope by MainScope() {

    companion object {
        // Singleton prevents multiple instances opening at the
        // same time.
        @Volatile
        private lateinit var instance: InternetConnectionManager
        private val listenerMap: HashMap<String, InternetConnectionListener> = hashMapOf()
        private val LOCK = Any()
        private var isConnectionLost = false

        fun getInstance() = instance

        fun initiate(context: Context) {
            synchronized(LOCK) {
                InternetConnectionManager().also {
                    instance = it
                    it.initManager(context)
                }
            }
        }
    }

    /**
     * Retrieves a system-level service for Connectivity manager to manage network connections.
     * Connectivity manager registers the {android.net.ConnectivityManager.NetworkCallback}
     * to receive notifications about changes in the system default network
     * */
    private fun initManager(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)
    }

    /**
     * Adds InternetConnectionListener whose methods are called when there is any change in the
     * network connection.
     * @param tag a unique value to identify the listener
     */
    fun addInternetConnectionListener(
        tag: String,
        internetConnectionListener: InternetConnectionListener
    ) {
        listenerMap[tag] = internetConnectionListener
    }

    /**
     * Removes InternetConnectionListener whose methods are called when there is any change in the
     * network connection.
     * @param tag a unique value to identify the listener
     */
    fun removeInternetConnectionListener(tag: String) {
        if (listenerMap.containsKey(tag)) {
            listenerMap.remove(tag)
        }
    }

    /**
     * Called when the framework connects and has declared a new network ready for use.
     * */
    override fun onAvailable(network: Network) {
        listenerMap.keys.forEach {
            val listener = listenerMap[it]
            listener?.onInternetAvailable()
        }
        if (isConnectionLost) {
            isConnectionLost = false
            ServicesBroadcastManager(AppController.applicationInstance).startInternetConnectivityService()
        }
    }

    /**
     * Called when the framework has a hard loss of the network or when the
     * graceful failure ends.
     * */
    override fun onLost(network: Network) {
        Log.d("InternetConn", "on lost in icm")
        isConnectionLost = true
        listenerMap.keys.forEach {
            val listener = listenerMap[it]
            listener?.onInternetLost()
        }

        ServicesBroadcastManager(AppController.applicationInstance).startInternetConnectivityService()
    }

    /**
     * Checks if internet connection is available or not by checking active network
     * */
    fun hasInternetConnection(context: Context?): Boolean {
        context ?: return false
        return checkHasActiveNetwork(context)
    }

    /**
     * Gets a currently active default data network and checks the presence of transport type
     * for wifi or cellular
     * */
    private fun checkHasActiveNetwork(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                return (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            }
        }
        return false
    }

    /**
     * Checks if internet is available or not by pinging private/public server.
     * Validates if there is any active network available. If there is any active network, checks
     * if the private server is reachable or not. If the private server is not reachable, checks
     * if the public server is reachable or not.
     * */
    fun checkInternetConnectionWithHost(context: Context): Boolean {
        var isAvailable = checkHasActiveNetwork(context)
        Log.d("NetPing", "is network available = $isAvailable")
        val isPublicHostAvailable: Boolean
        if (isAvailable) {
            isPublicHostAvailable = isPublicHostReachable()
            Log.d("NetPing", "is public server reachable = $isPublicHostAvailable")
            if (!isPublicHostAvailable) {
                isAvailable = false
            }
        }
        Log.d("NetPing", "checkInternetConnectionWithHost = $isAvailable")
        return isAvailable
    }

    /**
     * Checks if the public server is reachable
     * */
    private fun isPublicHostReachable(): Boolean {
        return pingServer(ApiClient.BASE_URL_CLIENTPORTAL)
    }

    /**
     * Checks if the server is reachable by making a connection to the URL. Returns true if the
     * connection status code is 200 else returns false
     * */
    private fun pingServer(url: String): Boolean {
        return try {
            val connection: HttpURLConnection =
                URL(url).openConnection() as HttpURLConnection
            connection.readTimeout = 1000
            connection.connectTimeout = 1000
            val code: Int = connection.responseCode
            connection.disconnect()
            Log.d("NetPing", "Internet connection host ping : $code")
            code == 200
        } catch (ex: IOException) {
            Log.d("NetPing", "Internet connection host ping exception : ${ex.message}")
            false
        }
    }


    fun getCurrentSignalStrength(): Int {
        return 0
    }

    interface InternetConnectionListener {
        fun onInternetAvailable()

        fun onInternetLost()

    }

    private fun calculateAverageNetworkStatus(): Int {

        return -1
    }

}