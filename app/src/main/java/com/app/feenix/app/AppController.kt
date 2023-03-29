package com.app.feenix.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.app.feenix.R
import com.app.feenix.feature.internet.ConnectivityTriggerHandler
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.notification.NotificationSystemManager
import com.app.feenix.utils.LocaleContextWrapper
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class AppController : Application() {

    private lateinit var localeContext: Context

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
        InternetConnectionManager.initiate(this)
        NotificationSystemManager.initiate(this)
        ConnectivityTriggerHandler.initiate()
        ConnectivityTriggerHandler.getInstance().initTrigger(this)

        updateAppLocaleContext()
    }

    fun updateAppLocaleContext() {
        localeContext = ContextWrapper(LocaleContextWrapper.wrapContext(baseContext))
    }

    fun getLocaleContext() = localeContext


}