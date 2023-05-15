package com.app.feenix.utils.customcomponents

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.app.feenix.app.AppController
import com.app.feenix.view.adapter.AutoCompleteAdapter
import com.google.gson.Gson
import org.json.JSONObject
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


    fun searchPlaces(
        ValuesEditext: String, context: Context, current_lat: Double?, current_lng: Double?,
        mapkey: String, listview: ListView
    ) {
        var predictions: PlacePredictions = PlacePredictions()
        var mAutoCompleteAdapter: AutoCompleteAdapter? = null
        val `object` = JSONObject()
        if (ValuesEditext.length > 3) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                getPlaceAutoCompleteUrl(
                    ValuesEditext, current_lat, current_lng, mapkey
                ),
                `object`, { response ->
                    val gson = Gson()
                    predictions = gson.fromJson(response.toString(), PlacePredictions::class.java)
                    if (mAutoCompleteAdapter == null) {
                        listview.visibility = View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            context,
                            predictions.getPlaces()!!
                        )
                        listview.adapter = mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                    } else {
                        listview.visibility = View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            context,
                            predictions.getPlaces()!!
                        )
                        listview.adapter = mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                        listview.invalidate()
                    }
                }
            ) { error -> Log.v("SearchResponse", error.toString()) }
            AppController.applicationInstance.addToRequestQueue(jsonObjectRequest)
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