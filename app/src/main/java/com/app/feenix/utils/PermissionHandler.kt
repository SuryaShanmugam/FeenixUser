package com.app.feenix.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.feenix.R

/**
 * This class is used to handle runtime permission targeting Android 6.0 and higher
 */
object PermissionHandler {

    private var mPermissionHandleListener: PermissionHandleListener? = null
    private var isFirstTime = false
    private var alertDialog: AlertDialog? = null

    fun checkPermission(
        activity: AppCompatActivity,
        permission: Array<String>,
        requestCode: Int,
        permissionHandleListener: PermissionHandleListener?
    ) {
        isFirstTime = false
        mPermissionHandleListener = permissionHandleListener
        if (isAllPermissionGranted(activity, permission)) {
            mPermissionHandleListener?.onPermissionGranted(requestCode)
        } else {
            for (value in permission) {
                if (activity.shouldShowRequestPermissionRationale(value)) {
                    Log.d("LocPer", "shouldShowRequestPermissionRationale - $value")
                    isFirstTime = true
                    break
                }
            }
            requestPermission(activity, permission, requestCode)
        }
    }

    private fun requestPermission(
        activity: AppCompatActivity,
        permission: Array<String>,
        requestCode: Int
    ) {
        activity.requestPermissions(permission, requestCode)
    }

    fun onRequestPermissionResult(
        activity: AppCompatActivity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray?
    ) {
        var permSt = ""
        permissions.forEach {
            permSt += "$it, "
        }
        var grRes = ""
        grantResults?.forEach {
            grRes += "$it, "
        }
        Log.d("LocPer", "onRequestPermissionResult : $permSt, grantResults = $grRes")
        if (isAllPermissionGranted(activity, permissions)) {
            mPermissionHandleListener?.onPermissionGranted(requestCode)
        } else {
            val permissionFailed = getFailedPermissions(activity, permissions as Array<String>)
            Log.d("LocPer", "permission failed : $permissions")
            if (permissionFailed.isNotEmpty()) {
                mPermissionHandleListener?.onShowPermissionSettingsDialog(permissionFailed)
            } else {
                mPermissionHandleListener?.onPermissionDenied(requestCode)
            }
        }
    }

    fun getFailedPermissions(
        activity: AppCompatActivity,
        permissions: Array<String>
    ): Array<String?> {
        val permissionFailed = arrayOfNulls<String>(permissions.size)
        var count = 0
        for (value in permissions) {
            if (!isPermissionGranted(activity, value)) {
                permissionFailed[count] = value
                count += 1
            }
        }
        return permissionFailed
    }

    @SuppressLint("InflateParams")
    fun showPermissionAlert(activity: AppCompatActivity, permission: Array<String?>): AlertDialog? {


        val permissionAlertDialog = Dialog(activity)
        permissionAlertDialog.setCancelable(false)
        permissionAlertDialog.setCanceledOnTouchOutside(false)
        permissionAlertDialog.setContentView(R.layout.dialog_permission_alert)
        permissionAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = permissionAlertDialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        permissionAlertDialog.show()

        val permission_alert_dialog_title_text_view =
            permissionAlertDialog.findViewById<TextView>(R.id.permission_alert_dialog_title_text_view)
        val permission_alert_dialog_content_text_view =
            permissionAlertDialog.findViewById<TextView>(R.id.permission_alert_dialog_content_text_view)
        val permission_alert_dialog_negative_button =
            permissionAlertDialog.findViewById<Button>(R.id.permission_alert_dialog_negative_button)
        val permission_alert_dialog_positive_button =
            permissionAlertDialog.findViewById<Button>(R.id.permission_alert_dialog_positive_button)
        permission_alert_dialog_content_text_view.text = getContent(permission)
        permission_alert_dialog_title_text_view.text = activity.getString(R.string.permission_title)
        permission_alert_dialog_negative_button.setOnClickListener {
            permissionAlertDialog.dismiss()
            mPermissionHandleListener?.onPermissionDontAllow()
        }
        permission_alert_dialog_positive_button.setOnClickListener {
            permissionAlertDialog.dismiss()
            mPermissionHandleListener?.onPermissionAllow()
        }
        return alertDialog
    }

    private fun getContent(permissionList: Array<String?>): String {
        if (permissionList.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            permissionList.contains(Manifest.permission.ACCESS_FINE_LOCATION) ||
            permissionList.contains(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                LocaleContextWrapper.getLocaleString(R.string.location_permission_msg)
            } else {
                LocaleContextWrapper.getLocaleString(R.string.bg_location_permission_msg)
            }
        }

        if (permissionList.contains(Manifest.permission.POST_NOTIFICATIONS)) {
            return LocaleContextWrapper.getLocaleString(R.string.push_notification_permission_msg)
        }

        var value = ""
        for (i in permissionList.indices) {
            value += permissionList[i]?.let { getPermissionMsg(it) }
            if (i != permissionList.size - 1) {
                value += "\n"
            }
        }
        return value
    }

    private fun getPermissionMsg(permission: String?): String {
        when (permission) {
            Manifest.permission.CALL_PHONE -> return LocaleContextWrapper.getLocaleString(R.string.call_permission_msg)
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION -> return LocaleContextWrapper.getLocaleString(
                R.string.location_permission_msg
            )
            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> return LocaleContextWrapper.getLocaleString(
                R.string.bg_location_permission_msg
            )
        }
        return ""
    }

    fun isAllPermissionGranted(
        context: Context,
        permissionList: Array<out String>
    ): Boolean {
        for (permission in permissionList) {
            if (!isPermissionGranted(context, permission)) {
                return false
            }
        }
        return true
    }

    private fun isPermissionGranted(
        context: Context,
        permission: String
    ): Boolean {
        return context.checkSelfPermission(
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    interface PermissionHandleListener {
        fun onPermissionGranted(requestCode: Int)
        fun onPermissionDenied(requestCode: Int)
        fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>)
        fun onPermissionDontAllow()
        fun onPermissionAllow()
    }

}