package com.app.feenix.handler

import android.app.Activity
import androidx.appcompat.app.AlertDialog

object AlertDialogHandler {

    fun showAlertDialog(activity: Activity, builder: AlertDialog.Builder): AlertDialog? {
        if (!activity.isDestroyed && !activity.isFinishing) {
            val dialog = builder.create()
            dialog.show()
            return dialog
        }
        return null
    }
}