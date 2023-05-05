package com.app.feenix.view.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.feature.internet.LocationConnectivityManager
import com.app.feenix.utils.LocaleContextWrapper
import com.app.feenix.utils.Log
import com.app.feenix.utils.customcomponents.AppToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * This activity defines common functionality and is responsible for handling common logic across all the activities.
 * BaseActivity should be the parent of all the activities in the project.
 */

open class BaseActivity : AppCompatActivity(),
    BaseView, CoroutineScope by MainScope() {
    override fun getContext() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BaseTag", "Base activity before super onCreate called")
        super.onCreate(savedInstanceState)
        Log.d("BaseTag", "Base activity set night mode off")
    }

    override fun onResume() {
        super.onResume()
        IntentFilter().let {
            val beaconConnectivityManager = LocationConnectivityManager.getInstance()
            it.addAction(beaconConnectivityManager.getLocationStateChangeAction())
            registerReceiver(broadcastReceiver, it)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleContextWrapper.wrapContext(it) })
    }


    override fun showMessage(message: String?) {
        message?.let { AppToast.showToast(this, it) }
    }

    override fun showMessage(messageId: Int) {
        showMessage(getString(messageId))
    }


    override fun hasInternetConnection(): Boolean {
        return InternetConnectionManager.getInstance().hasInternetConnection(this)
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val beaconConnectivityManager = LocationConnectivityManager.getInstance()
            when (intent?.action) {
                beaconConnectivityManager.getLocationStateChangeAction() -> {
                    beaconConnectivityManager.handleLocationStateChanged()
                }
            }
        }
    }

}