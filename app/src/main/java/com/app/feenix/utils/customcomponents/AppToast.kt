package com.app.feenix.utils.customcomponents

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.app.feenix.R

object AppToast {

    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            toast.show()
        } else {
            toast.view?.also {
                it.setPadding(context.resources.getDimensionPixelSize(R.dimen.dp_10))
                it.setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
                it.findViewById<TextView>(android.R.id.message).setTextColor(Color.WHITE)
                toast.show()
            }
        }
    }
}