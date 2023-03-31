package com.app.feenix.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import cbs.com.bmr.Utilities.MyActivity
import com.app.feenix.app.Constant
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.databinding.ActivitySplashBinding
import com.app.feenix.utils.CodeSnippet
import com.app.feenix.utils.Log
import com.app.feenix.utils.PermissionHandler
import com.app.feenix.view.activities.Walkthrough.WalkthroughActivity
import com.app.feenix.view.base.BaseActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {


    private val notificationPermissionReqCode = 87
    private var isPermissionCheckCompleted = false
    private var alertDialog: AlertDialog? = null
    private lateinit var binding: ActivitySplashBinding
    var handler = Handler()
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        startAppForegroundService()
    }

    private fun initObject() {
        mContext = this@SplashActivity
        handler.postDelayed({
            MyActivity.launchClearStack(mContext!!, WalkthroughActivity::class.java)
        }, 3000)


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


    }


    private fun checkHasLocationPermission() {
        PermissionHandler.checkPermission(
            this,
            getAllLocationPermissions(),
            notificationPermissionReqCode,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        checkHasNotificationPermission()
                    }
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkHasNotificationPermission() {
        PermissionHandler.checkPermission(
            this,
            getPushNotificationPermission(),
            notificationPermissionReqCode,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {
                }

                override fun onPermissionDenied(requestCode: Int) {
                    isPermissionCheckCompleted = true
                }

                override fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>) {
                    if (alertDialog == null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            alertDialog = PermissionHandler.showPermissionAlert(
                                this@SplashActivity,
                                PermissionHandler.getFailedPermissions(
                                    this@SplashActivity,
                                    getPushNotificationPermission()
                                )
                            )
                        }
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
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("LocPer", "getAllLocationPermissions: >= Q")
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                Log.d("LocPer", "getAllLocationPermissions: < Q")
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getPushNotificationPermission() = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

}