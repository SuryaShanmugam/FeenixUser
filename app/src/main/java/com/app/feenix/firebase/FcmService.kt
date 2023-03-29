package com.app.feenix.firebase

import com.app.feenix.utils.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMTOken", token)

    }


}