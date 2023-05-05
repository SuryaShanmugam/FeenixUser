package com.app.feenix.feature.internet

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

class LocationStateManager(context: Context) {

    private var permissionStateChangeCallback: PermissionStateChangeCallback? = null
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isLocationServicesEnabled(): Boolean {
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun setStateChangeCallback(permissionStateChangeCallback: PermissionStateChangeCallback) {
        this.permissionStateChangeCallback = permissionStateChangeCallback
    }

    fun getLocationStateChangeAction() = LocationManager.PROVIDERS_CHANGED_ACTION

    fun handleStateChangeAction() {
        permissionStateChangeCallback?.onLocationPermissionStateChanged(isLocationServicesEnabled())
    }

    interface PermissionStateChangeCallback {

        fun onLocationPermissionStateChanged(isEnabled: Boolean)

    }
}