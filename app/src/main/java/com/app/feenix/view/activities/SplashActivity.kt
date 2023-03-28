package com.app.feenix.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.app.feenix.R
import com.app.feenix.app.Constant
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.databinding.ActivitySplashBinding
import com.app.feenix.feature.internet.ConnectivityTriggerHandler
import com.app.feenix.feature.internet.InternetConnectionLayout
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.utils.CodeSnippet
import com.app.feenix.utils.Log
import com.app.feenix.utils.PermissionHandler
import com.app.feenix.view.base.BaseActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), InternetConnectionManager.InternetConnectionListener {


    private val notificationPermissionReqCode = 87
    private var isPermissionCheckCompleted = false
    private var alertDialog: AlertDialog? = null
    private lateinit var internetConnectionLayout: InternetConnectionLayout
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash)
        internetConnectionLayout = binding.activitySettingsInternetConnectionLayout.root
        internetConnectionLayout.init(this)
        ConnectivityTriggerHandler.getInstance().initTrigger(this)
        startAppForegroundService()
    }

    private fun startAppForegroundService() {
        val bundle = Bundle()
        bundle.putBoolean(Constant.ACTIVITY_STARTED_SERVICE, true)
        ServicesBroadcastManager(this).startForegroundService(extras = bundle)

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onResume() {
        super.onResume()
        checkHasLocationPermission()
        internetConnectionLayout.apply {
            onResume()
            registerInternetConnectionListener("SplashActivity", this@SplashActivity)
        }

    }

    override fun onPause() {
        super.onPause()
        internetConnectionLayout.unregisterInternetConnectionListener()
    }


    private fun checkHasLocationPermission() {
        PermissionHandler.checkPermission(
            this,
            getAllLocationPermissions(),
            notificationPermissionReqCode,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {
                }

                override fun onPermissionDenied(requestCode: Int) {
                    isPermissionCheckCompleted = true
                }

                override fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>) {
                    if (alertDialog == null) {
                        alertDialog = PermissionHandler.showPermissionAlert(
                            this@SplashActivity,
                            PermissionHandler.getFailedPermissions(
                                this@SplashActivity,
                                getAllLocationPermissions()
                            )
                        )
                    }
                }

                override fun onPermissionDontAllow() {
                    isPermissionCheckCompleted = true
                    alertDialog = null
                }

                override fun onPermissionAllow() {
                    alertDialog = null
                    CodeSnippet.navigateToAppSettings(this@SplashActivity)
                }
            })
    }

    private fun getAllLocationPermissions(): Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else getBeaconLocationPermissions()

    private fun getBeaconLocationPermissions() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("LocPer", "getAllBeaconPermissions: >= Q")
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            Log.d("LocPer", "getAllBeaconPermissions: < Q")
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getPushNotificationPermission() = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    override fun onInternetAvailable() {

        internetConnectionLayout.onInternetAvailable()

    }

    override fun onInternetLost() {
        internetConnectionLayout.onInternetLost()
    }
}