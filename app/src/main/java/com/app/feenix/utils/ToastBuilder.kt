package cbs.com.bmr.Utilities

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Bobby on 1/13/2018
 */
object ToastBuilder {

    fun build(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun failure(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun showSnackbar(
        view: View?,
        message: String?,
        duration: Int
    ) {
        Snackbar.make(view!!, message!!, duration).show()
    }


}