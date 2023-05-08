package com.app.feenix.feature.internet

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.feenix.app.AppController
import com.app.feenix.utils.AppLifecycleObserver
import com.app.feenix.utils.CodeSnippet
import com.app.feenix.utils.LocaleContextWrapper
import com.app.feenix.utils.PermissionHandler

/**
 * This class is responsible for starting/stopping beacon scanning. It monitors if any beacon data is
 * received in the scan callback. It also does check if all the necessary permissions are granted
 * and bluetooth & location services are enabled before starting or in the middle of scanning process.
 * Respective error methods are triggered if any permissions are not granted or if bluetooth or
 * location services disabled.
 */
class LocationConnectivityManager : LocationStateManager.PermissionStateChangeCallback {

    companion object {
        // Singleton prevents multiple instances opening at the
        // same time.
        @Volatile
        private lateinit var instance: LocationConnectivityManager
        private val LOCK = Any()
        private const val beaconPermissionReqCode = 60
        private const val beaconBgPermissionReqCode = 61


        fun getInstance() = instance

        fun initiate(context: Context) {
            synchronized(LOCK) {
                LocationConnectivityManager().also {
                    instance = it
                    it.initBeaconManager(context)

                }
            }
        }
    }

    private fun initBeaconManager(context: Context) {
        locationStateManager = AppController.applicationInstance.locationStateManager()
        locationStateManager.setStateChangeCallback(this)
        monitorBeaconPermissionsWhileScanning()
    }


    private var beaconConnectivityCallback: LocationConnectivityCallback? = null
    private val tags = "BeaconCM"
    private lateinit var locationStateManager: LocationStateManager

    fun setLocationConnectivityCallback(
        beaconConnectivityCallback: LocationConnectivityCallback
    ) {
        this.beaconConnectivityCallback = beaconConnectivityCallback
    }

    private fun monitorBeaconPermissionsWhileScanning() {

        if (!AppLifecycleObserver.isAppOnForeground) {
            if (locationStateManager.isLocationServicesEnabled()) {

            }
        }
    }

    fun checkHasRequiredPermission(activity: AppCompatActivity) {
        PermissionHandler.checkPermission(
            activity,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getForegroundLocationPermission()
            } else {
                getAllBeaconPermissions()
            },
            beaconPermissionReqCode,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {
                    if (requestCode == beaconPermissionReqCode) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            beaconConnectivityCallback?.requestBackgroundLocationUserConsent()
                        }
                    }
                }

                override fun onPermissionDenied(requestCode: Int) {
                }

                override fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>) {
                    beaconConnectivityCallback?.onShowLocationPermissionDialog(
                        getAllBeaconPermissions()
                    )
                }

                override fun onPermissionDontAllow() {
                    beaconConnectivityCallback?.onDismissLocationPermissionDialog()
                }

                override fun onPermissionAllow() {
                    beaconConnectivityCallback?.onDismissLocationPermissionDialog()
                    CodeSnippet.navigateToAppSettings(activity)
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkHasBgLocationPermission(activity: AppCompatActivity) {
        PermissionHandler.checkPermission(
            activity,
            getBackgroundLocationPermission(),
            beaconBgPermissionReqCode,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {

                    if (!locationStateManager.isLocationServicesEnabled()) {
                        beaconConnectivityCallback?.showLocationErrorAlert()
                    }
                }

                override fun onPermissionDenied(requestCode: Int) {
                }

                override fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>) {
                    beaconConnectivityCallback?.onShowLocationPermissionDialog(
                        getAllBeaconPermissions()
                    )
                }

                override fun onPermissionDontAllow() {
                    beaconConnectivityCallback?.onDismissLocationPermissionDialog()
                }

                override fun onPermissionAllow() {
                    beaconConnectivityCallback?.onDismissLocationPermissionDialog()
                    CodeSnippet.navigateToAppSettings(activity)
                }
            })
    }

    private val LOCATION = 0


    fun onRequestPermissionsResult(
        activity: AppCompatActivity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionHandler.onRequestPermissionResult(
            activity,
            requestCode,
            permissions,
            grantResults
        )
    }

    fun isAllBeaconPermissionGranted(): Boolean {
        return PermissionHandler.isAllPermissionGranted(
            AppController.applicationInstance,
            getAllBeaconPermissions()
        )
    }

    fun isBeaconLocationPermissionGranted(): Boolean {
        return PermissionHandler.isAllPermissionGranted(
            AppController.applicationInstance,
            getBeaconLocationPermissions()
        )
    }

    /**
     * Creates a permission list required for beacon scanning
     */
    private fun getAllBeaconPermissions(): Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else getBeaconLocationPermissions()

    private fun getBeaconLocationPermissions() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }


    private fun getForegroundLocationPermission(): Array<String> =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getBackgroundLocationPermission(): Array<String> =
        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)


    fun getLocationStateChangeAction(): String {
        return locationStateManager.getLocationStateChangeAction()
    }


    fun handleLocationStateChanged() {
        locationStateManager.handleStateChangeAction()

    }


    fun getString(id: Int) = LocaleContextWrapper.getLocaleString(id)
    override fun onLocationPermissionStateChanged(isEnabled: Boolean) {


    }

}