package com.app.feenix.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.feenix.viewmodel.OtpReceivedInterface
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver: BroadcastReceiver() {

    private val TAG = "SmsBroadcastReceiver"
    var otpReceiveInterface: OtpReceivedInterface? = null

    fun setOnOtpListeners(otpReceiveInterface: OtpReceivedInterface?) {
        this.otpReceiveInterface = otpReceiveInterface
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val mStatus = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            Log.e(TAG, "" + mStatus!!.statusCode)
            when (mStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Log.e(TAG, message!!)
                    otpReceiveInterface!!.onOtpReceived(message)
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.e(TAG, "onReceive: failure")
                    otpReceiveInterface!!.onOtpTimeout()
                }
            }
        }
    }
}