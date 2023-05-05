package com.app.feenix.feature.internet

interface LocationConnectivityCallback {
    fun onShowLocationPermissionDialog(permissions: Array<String>)
    fun onDismissLocationPermissionDialog()
    fun requestBackgroundLocationUserConsent()
    fun showLocationErrorAlert()
}