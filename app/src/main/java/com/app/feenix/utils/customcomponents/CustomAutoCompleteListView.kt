package com.app.feenix.utils.customcomponents

import android.content.Context
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

open class CustomAutoCompleteListView(context: Context) {

    companion object {
        private var instance: CustomAutoCompleteListView? = null

        fun getInstance(context: Context): CustomAutoCompleteListView {
            if (instance == null)  // NOT thread safe!
                instance = CustomAutoCompleteListView(context)

            return instance!!
        }
    }

    open fun getPlaceAutoCompleteUrl(
        input: String?,
        current_lat: Double?,
        current_lng: Double?,
        dynamicMapkey: String?
    ): String? {
        val urlString = StringBuilder()
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json")
        urlString.append("?input=")
        try {
            urlString.append(URLEncoder.encode(input, "utf8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        urlString.append("&location=")
        urlString.append(current_lat.toString() + "," + current_lng.toString())
        urlString.append("&radius=500&language=en")
        urlString.append("&key=" + dynamicMapkey)
        Log.e("FINAL URL:::   ", urlString.toString())
        return urlString.toString()
    }

}