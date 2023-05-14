package com.app.feenix.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.app.feenix.R
import com.app.feenix.feature.internet.ConnectivityTriggerHandler
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.feature.internet.LocationConnectivityManager
import com.app.feenix.feature.internet.LocationStateManager
import com.app.feenix.notification.NotificationSystemManager
import com.app.feenix.utils.LocaleContextWrapper
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.intercom.android.sdk.Intercom

class AppController : Application() {

    private lateinit var localeContext: Context
    private var locationStateManager: LocationStateManager? = null
    companion object {
        private var instance: AppController? = null
        val applicationInstance: AppController
            get() = instance ?: throw IllegalStateException("Feenix User app not created")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Graphik-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                )
            )
                .build()
        )
        LocationConnectivityManager.initiate(this)
        InternetConnectionManager.initiate(this)
        NotificationSystemManager.initiate(this)
        ConnectivityTriggerHandler.initiate()
        ConnectivityTriggerHandler.getInstance().initTrigger(this)
        Intercom.initialize(
            this,
            "android_sdk-a4fa553e3cb668a6b71c920df93b2a3ca7453614",
            "z63zjeha"
        )

        updateAppLocaleContext()
    }

    fun updateAppLocaleContext() {
        localeContext = ContextWrapper(LocaleContextWrapper.wrapContext(baseContext))
    }

    fun getLocaleContext() = localeContext
    fun locationStateManager(): LocationStateManager {
        if (locationStateManager == null) {
            locationStateManager = LocationStateManager(this)
        }
        return locationStateManager!!
    }

    private val TAG = AppController::class.java.simpleName
    private var mRequestQueue: RequestQueue? = null
    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue()?.add<T>(req)
    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }
}