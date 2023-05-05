package com.app.feenix.firebase

import com.app.feenix.app.MyPreference
import com.app.feenix.utils.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FcmService : FirebaseMessagingService() {

    var myPreference: MyPreference? = null
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        myPreference = MyPreference(this)
        myPreference?.fcmToken = token
        Log.error("FCMTOken", token)

    }


}