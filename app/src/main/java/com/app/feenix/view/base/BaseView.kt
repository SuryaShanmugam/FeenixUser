package com.app.feenix.view.base

import android.content.Context

interface BaseView {

    fun getContext(): Context?
    fun showMessage(message: String?)
    fun showMessage(messageId: Int)
    fun hasInternetConnection(): Boolean

}