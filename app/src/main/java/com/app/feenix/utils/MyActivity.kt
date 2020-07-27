package cbs.com.bmr.Utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by Bobby on 1/13/2018
 */
object MyActivity {

    fun launch(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }

    fun launchWithBundle(context: Context, cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun launchWithResult(fragment: Fragment, cls: Class<*>, requestCode: Int) {
        val intent = Intent(fragment.context, cls)
        fragment.startActivityForResult(intent, requestCode)
    }

    fun launchClearTop(context: Context, cls: Class<*>?) {
        val intent = Intent(context, cls)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    fun launchWithResult(activity: Activity, cls: Class<*>, requestCode: Int) {
        val intent = Intent(activity, cls)
        activity.startActivityForResult(intent, requestCode)
    }

    fun launchWithBundleResult(
        fragment: Fragment,
        cls: Class<*>,
        bundle: Bundle,
        requestCode: Int
    ) {
        val intent = Intent(fragment.context, cls)
        intent.putExtras(bundle)
        fragment.startActivityForResult(intent, requestCode)
    }

    fun launchWithBundleResult(
        activity: Activity,
        cls: Class<*>,
        bundle: Bundle,
        requestCode: Int
    ) {
        val intent = Intent(activity, cls)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestCode)
    }

    fun launchClearStack(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun launchWithBundleClearStack(context: Context, cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun launchDialPad(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }


}