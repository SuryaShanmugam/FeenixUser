package com.app.biu.model.RequestModel.ResponseModel


data class SignInMobileResponse(
    val success: Boolean?,
    val data: SignInMobileResData?,
    val message: String?,
)

data class SignInMobileResData(
    val id: String?,
    val first_name: String?,
    val last_name: String?,
    val payment_mode: String?,
    val email: String?,
    val picture: String?,
    val device_token: String?,
    val device_id: String?,
    val device_type: String?,
    val login_by: String?,
    val social_unique_id: String?,
    val referal: String?,
    val referral_used: String?,
    val mobile: String?,
    val latitude: String?,
    val longitude: String?,
    val postal_code: String?,
    val stripe_cust_id: String?,
    val wallet_balance: String?,
    val rating: String?,
    val rating_count: String?,
    val otp: String?,
    val otp_activation: String?,
    val country_code: String?,
    val archive: String?,
    val fleet: String?,
    val marketer: String?,
    val driver_referred: String?,
    val user_referred: String?,
    val android_app_version: String?,
    val ios_app_version: String?,
    val access_token: String?,
    val new: String?
)


