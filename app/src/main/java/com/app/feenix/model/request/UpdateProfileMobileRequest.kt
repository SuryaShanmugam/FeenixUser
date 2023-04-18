package com.app.feenix.model.request

data class UpdateProfileMobileRequest(
    val mobile: String?, val country_code: String?
)
data class UpdateProfileNameRequest(
    val first_name: String?, val last_name: String?
)
data class UpdateProfileEmailRequest(
    val email: String?
)
data class  VerifyOTPRequest(
    val otp: String?
)

data class UpdateProfileRequest(
    val first_name: String?, val last_name: String?,
    val mobile: String?, val country_code: String?,val email: String?
)