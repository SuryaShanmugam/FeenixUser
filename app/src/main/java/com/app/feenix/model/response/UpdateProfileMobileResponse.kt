package com.app.feenix.model.response

data class UpdateProfileMobileResponse(
    val success: Boolean,
    val data: UpdateProfileMobileData?
)
data class UpdateProfileMobileData(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val picture: String,
    val payment_mode: String,
    val device_token: String,
    val device_id: String,
    val device_type: String,
    val login_by: String,
    val social_unique_id: String,
    val referal: String,
    val referral_used: String,
    val mobile: String,
    val latitude: String,
    val longitude: String,
    val postal_code: String,
    val stripe_cust_id: String,
    val wallet_balance: String,
    val rating: String,
    val rating_count: String,
    val otp: String,
    val otp_activation: String,
    val country_code: String,
    val archive: String,
    val fleet: String,
    val marketer: String,
    val driver_referred: String,
    val user_referred: String,
    val android_app_version: String,
    val ios_app_version: String,
    val user_referral: String,
    val driver_referral: String,
    val currency: String,
    val referral_bonus: String,
    val time_out: String,
    val welcome_image: String,
    val sos_number: String,
    val feenix_sos_number: String,
    val android_user_version: String,
    val ios_user_version: String,
    val surge: String,
    val android_user_mapkey: String,
    val total_request: String,
    val completed_request: String,
    val cancelled_request: String,
    val last_trip_status: String,
   // val last_booking_date: LastBookingDate?,
    val active_request_flow: String,
    val active_request_id: String
)

data class LastBookingDate(
    val date: String
)