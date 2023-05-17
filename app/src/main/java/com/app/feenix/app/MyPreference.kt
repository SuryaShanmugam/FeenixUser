package com.app.feenix.app

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64


class MyPreference(context: Context) {

    private val mPreferencesUser: SharedPreferences
    private val mPreferencesMisc: SharedPreferences

    init {
        mPreferencesUser = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        mPreferencesMisc = context.getSharedPreferences(PREF_MISC, Context.MODE_PRIVATE)
    }

    var id: Int
        get() = mPreferencesUser.getInt(encode(ID), -1)
        set(id) = mPreferencesUser.edit().putInt(encode(ID), id).apply()

    var mobile: String?
        get() = mPreferencesUser.getString(encode(MOBILE), null)
        set(mobile) = mPreferencesUser.edit().putString(encode(MOBILE), mobile).apply()

    var email: String?
        get() = mPreferencesUser.getString(encode(EMAIL), null)
        set(email) = mPreferencesUser.edit().putString(encode(EMAIL), email).apply()

    var token: String?
        get() = mPreferencesUser.getString(encode(TOKEN), "")
        set(token) = mPreferencesUser.edit().putString(encode(TOKEN), token).apply()

    val userToken = "Bearer $token"

    var profilePic: String?
        get() = mPreferencesUser.getString(encode(PROFILE_PIC), null)
        set(profilePic) = mPreferencesUser.edit().putString(encode(PROFILE_PIC), profilePic).apply()
    var Username: String?
        get() = mPreferencesUser.getString(encode(USERNAME), null)
        set(username) = mPreferencesUser.edit().putString(encode(USERNAME), username).apply()


    var isFirstTime: Boolean
        get() = mPreferencesMisc.getBoolean(encode(FIRST_TIME), true)
        set(firstTime) = mPreferencesMisc.edit().putBoolean(encode(FIRST_TIME), firstTime).apply()

    var fcmToken: String?
        get() = mPreferencesMisc.getString(encode(FCM_TOKEN), null)
        set(fcmToken) = mPreferencesMisc.edit().putString(encode(FCM_TOKEN), fcmToken).apply()

    var dynamicMapkey: String?
        get() = mPreferencesMisc.getString(encode(DYNAMIC_MAP_KEY), null)
        set(dynamicMapkey) = mPreferencesMisc.edit().putString(encode(DYNAMIC_MAP_KEY), dynamicMapkey).apply()
    var firstName: String?
        get() = mPreferencesUser.getString(encode(FIRST_NAME), null)
        set(firstName) = mPreferencesUser.edit().putString(encode(FIRST_NAME), firstName).apply()
    var lastName: String?
        get() = mPreferencesUser.getString(encode(LAST_NAME), null)
        set(lastName) = mPreferencesUser.edit().putString(encode(LAST_NAME), lastName).apply()

    var countryCode: String?
        get() = mPreferencesUser.getString(encode(COUNTRY_CODE), null)
        set(countryCode) = mPreferencesUser.edit().putString(encode(COUNTRY_CODE), countryCode).apply()

    var ReferralCode: String?
        get() = mPreferencesUser.getString(encode(REFFERAL_CODE), null)
        set(ReferralCode) = mPreferencesUser.edit().putString(encode(REFFERAL_CODE), ReferralCode).apply()

    var walletBal: String?
        get() = mPreferencesUser.getString(encode(WALLET_BAL), null)
        set(walletBal) = mPreferencesUser.edit().putString(encode(WALLET_BAL), walletBal).apply()

    var welcomeImage: String?
        get() = mPreferencesUser.getString(encode(WELCOME_IMAGE), null)
        set(welcomeImage) = mPreferencesUser.edit().putString(encode(WELCOME_IMAGE), welcomeImage)
            .apply()
    var fleet: String?
        get() = mPreferencesUser.getString(encode(FLEET), null)
        set(fleet) = mPreferencesUser.edit().putString(encode(FLEET), fleet).apply()

    var sosNumber: String?
        get() = mPreferencesUser.getString(encode(SOS_NUMBER), null)
        set(sosNumber) = mPreferencesUser.edit().putString(encode(SOS_NUMBER), sosNumber).apply()

    var TotalRequest: String?
        get() = mPreferencesUser.getString(encode(TOTAL_REQUEST), null)
        set(TotalRequest) = mPreferencesUser.edit().putString(encode(TOTAL_REQUEST), TotalRequest)
            .apply()

    var CancelledRequest: String?
        get() = mPreferencesUser.getString(encode(CANCELLED_REQUEST), null)
        set(CancelledRequest) = mPreferencesUser.edit()
            .putString(encode(CANCELLED_REQUEST), CancelledRequest).apply()
    var CompletedRequest: String?
        get() = mPreferencesUser.getString(encode(COMPLETED_REQUEST), null)
        set(CompletedRequest) = mPreferencesUser.edit()
            .putString(encode(COMPLETED_REQUEST), CompletedRequest).apply()
    var LastBookingDate: String?
        get() = mPreferencesUser.getString(encode(LAST_BOOKING_DATE), null)
        set(CompletedRequest) = mPreferencesUser.edit()
            .putString(encode(LAST_BOOKING_DATE), CompletedRequest).apply()

    var LastBookingStatus: String?
        get() = mPreferencesUser.getString(encode(LAST_BOOKING_STATUS), null)
        set(CompletedRequest) = mPreferencesUser.edit()
            .putString(encode(LAST_BOOKING_STATUS), CompletedRequest).apply()

    var CurrentRequestId: String?
        get() = mPreferencesUser.getString(encode(CURRENT_REQUEST_ID), null)
        set(CurrentRequestId) = mPreferencesUser.edit()
            .putString(encode(CURRENT_REQUEST_ID), CurrentRequestId).apply()
    var TripSearchingStatus: String?
        get() = mPreferencesUser.getString(encode(TRIP_SEARCHING_STATUS), null)
        set(CurrentRequestId) = mPreferencesUser.edit()
            .putString(encode(TRIP_SEARCHING_STATUS), CurrentRequestId).apply()

    fun clearUser() {

        mPreferencesUser.edit().clear().apply()
    }

    private fun encode(data: String): String {
        return Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
    }

    companion object {
        private const val PREF_USER = "user"
        private const val PREF_MISC = "misc"
        private const val ID = "id"
        private const val MOBILE = "mobile"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val PROFILE_PIC = "profile_pic"
        private const val FIRST_TIME = "first_time"
        private const val FCM_TOKEN = "fcm_token"
        private const val USERNAME = "username"
        private const val DYNAMIC_MAP_KEY = "usermapkey"
        private const val FIRST_NAME = "firstname"
        private const val LAST_NAME = "lastname"
        private const val COUNTRY_CODE = "countrycode"
        private const val REFFERAL_CODE = "referalcode"
        private const val WELCOME_IMAGE = "welcome_image"
        private const val WALLET_BAL = "wallet_bal"
        private const val FLEET = "fleet"
        private const val SOS_NUMBER = "sos_number"
        private const val TOTAL_REQUEST = "total_request"
        private const val CANCELLED_REQUEST = "cancelled_request"
        private const val COMPLETED_REQUEST = "completed_request"
        private const val LAST_BOOKING_DATE = "LastBooking_date"
        private const val LAST_BOOKING_STATUS = "LastBooking_status"
        private const val CURRENT_REQUEST_ID = "current_requestId"
        private const val TRIP_SEARCHING_STATUS = "trip_searching_status"
    }
}
