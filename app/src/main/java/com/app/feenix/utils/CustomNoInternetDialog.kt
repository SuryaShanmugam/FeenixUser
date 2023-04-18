package com.app.feenix.utils

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R

open class CustomNoInternetDialog(context: Context) {
    private var dialog: Dialog? = null

    companion object {
        private var instance: CustomNoInternetDialog? = null

        fun getInstance(context: Context): CustomNoInternetDialog {
            if (instance == null)  // NOT thread safe!
                instance = CustomNoInternetDialog(context)

            return instance!!
        }
    }

    @Suppress("DEPRECATION")
    fun showDialog(context: Context) {
        try {
            dialog = Dialog(context)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            val window: Window = dialog!!.getWindow()!!
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)
            val buttontry: Button = dialog!!.findViewById(R.id.dismissButton)

            buttontry.setOnClickListener {
                val ConnectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = ConnectionManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected == true) {
                    hideDialog()
                } else {
                    ToastBuilder.build(context,"No Internet, Please Try Again")
                }
            }
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun hideDialog() {
        dialog!!.dismiss()
    }


}