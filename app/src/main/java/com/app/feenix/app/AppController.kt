package com.app.feenix.app

import android.app.Application
import com.app.feenix.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
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
    }

}