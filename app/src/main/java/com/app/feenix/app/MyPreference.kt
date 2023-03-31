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
    }
}
