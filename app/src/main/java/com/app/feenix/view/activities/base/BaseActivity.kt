package com.app.feenix.view.activities.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.utils.LocaleContextWrapper
import com.app.feenix.utils.Log
import com.app.feenix.utils.customcomponents.AppToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * This activity defines common functionality and is responsible for handling common logic across all the activities.
 * BaseActivity should be the parent of all the activities in the project.
 */

open class BaseActivity : AppCompatActivity(),
    BaseView, CoroutineScope by MainScope() {
    override fun getContext() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BaseTag", "Base activity before super onCreate called")
        super.onCreate(savedInstanceState)
        Log.d("BaseTag", "Base activity set night mode off")
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleContextWrapper.wrapContext(it) })
    }


    override fun showMessage(message: String?) {
        message?.let { AppToast.showToast(this, it) }
    }

    override fun showMessage(messageId: Int) {
        showMessage(getString(messageId))
    }


    override fun hasInternetConnection(): Boolean {
        return InternetConnectionManager.getInstance().hasInternetConnection(this)
    }


}