package com.app.feenix.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.app.feenix.app.AppController
import java.util.*


class LocaleContextWrapper {

    companion object {

        fun wrapContext(context: Context): Context {
            return updateBaseContextLocale(context)
        }

        private fun updateBaseContextLocale(context: Context): Context {
            val locale = Locale("English")
            Log.d("LangChange", "updatedBaseLocale = $locale")
            Locale.setDefault(locale)
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            return context.createConfigurationContext(configuration)
        }


        fun getResources(): Resources {
            return AppController.applicationInstance.getLocaleContext().resources
        }

        fun getLocaleString(id: Int): String {
            return getResources().getString(id)
        }
    }
}