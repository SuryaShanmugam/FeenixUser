package com.app.feenix.view.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.app.Constant
import com.app.feenix.app.MyPreference
import com.app.feenix.broadcastreceiver.ServicesBroadcastManager
import com.app.feenix.databinding.ActivitySplashBinding
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.utils.CodeSnippet
import com.app.feenix.utils.Log
import com.app.feenix.utils.PermissionHandler
import com.app.feenix.view.ui.Walkthrough.WalkthroughActivity
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.webservices.SignIn.SignInService
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), IGetProfileData {


    private val notificationPermissionReqCode = 87
    private var isPermissionCheckCompleted = false
    private var alertDialog: AlertDialog? = null
    private lateinit var binding: ActivitySplashBinding
    var handler = Handler()
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private var authService: SignInService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        startAppForegroundService()
    }

    private fun initObject() {
        mContext = this@SplashActivity
        myPreference = MyPreference(mContext!!)
        authService = SignInService()
        authService!!.SignInService(this@SplashActivity)
        myPreference?.fcmToken =
            "fPaQiC4NhHw:APA91bFsGrKGYNQTcI_RvAu-rRPS4TDtBJuDFlzh7hBy5Q5R1plbhcysP7MUqUYXbuQtAuhZb9gbulKMuEWaEwH3m500CNUSNCk6cG7rf_V7xYe53J0J3QDBx8hCKxiMmdo-BVN4QVKd"
        handler.postDelayed({
            if (myPreference?.token!!.isNotEmpty()) {

                if (hasInternetConnection()) {
                    authService?.getProfileDetails(this)
                } else {
                    ToastBuilder.build(mContext!!, "No Internet, Please try again")

                }

            } else {
                MyActivity.launchClearStack(mContext!!, WalkthroughActivity::class.java)
            }


        }, 3000)


    }

    private fun getProfileDetails() {


    }

    private fun startAppForegroundService() {
        val bundle = Bundle()
        bundle.putBoolean(Constant.ACTIVITY_STARTED_SERVICE, true)
        ServicesBroadcastManager(this).startForegroundService(extras = bundle)

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHandler.onRequestPermissionResult(
            this,
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onGetProfileResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if (updateProfileMobileResponse.success) {
            myPreference?.dynamicMapkey = updateProfileMobileResponse.data?.android_user_mapkey
            myPreference?.email = updateProfileMobileResponse.data?.email
            myPreference?.firstName = updateProfileMobileResponse.data?.first_name
            myPreference?.lastName = updateProfileMobileResponse.data?.last_name
            myPreference?.mobile = updateProfileMobileResponse.data?.mobile
            myPreference?.countryCode = updateProfileMobileResponse.data?.country_code
            myPreference?.ReferralCode = updateProfileMobileResponse.data?.referal
            myPreference?.id = updateProfileMobileResponse.data?.id!!
            myPreference?.welcomeImage = updateProfileMobileResponse.data.welcome_image
            myPreference?.sosNumber = updateProfileMobileResponse.data.sos_number
            myPreference?.profilePic = updateProfileMobileResponse.data.picture
            myPreference?.fleet = updateProfileMobileResponse.data.fleet
            myPreference?.walletBal = updateProfileMobileResponse.data.wallet_balance
            myPreference?.TotalRequest = updateProfileMobileResponse.data.total_request
            myPreference?.CancelledRequest = updateProfileMobileResponse.data.cancelled_request
            myPreference?.CompletedRequest = updateProfileMobileResponse.data.completed_request
            myPreference?.LastBookingStatus = updateProfileMobileResponse.data.last_trip_status
            myPreference?.LastBookingDate = updateProfileMobileResponse.data.last_booking_date?.date
            moveHomeActivity(
                updateProfileMobileResponse.data.active_request_flow,
                updateProfileMobileResponse.data.active_request_id
            )

        } else {
            ToastBuilder.build(mContext!!, "Response Error")
        }
    }

    private fun moveHomeActivity(activeRequestFlow: String, activeRequestId: String) {

        MyActivity.launchClearStack(mContext!!, HomeActivity::class.java)

    }
}