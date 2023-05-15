package com.app.feenix.viewmodel

import org.json.JSONObject


interface ICallback {
    fun onSuccess(result: JSONObject?)
}