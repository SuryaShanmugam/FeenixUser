package com.app.feenix.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.app.feenix.R

open class CustomProgressBar(context: Context) {
    private var dialog: Dialog? = null

    companion object {
        private var instance: CustomProgressBar? = null

        fun getInstance(context: Context): CustomProgressBar {
            if (instance == null)  // NOT thread safe!
                instance = CustomProgressBar(context)

            return instance!!
        }
    }

    @Suppress("DEPRECATION")
    fun showDialog(context: Context?) {
        try {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setContentView(R.layout.dialog_custom_progressbar)
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun hideDialog() {
        dialog!!.dismiss()
    }


}