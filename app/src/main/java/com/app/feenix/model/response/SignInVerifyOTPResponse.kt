package com.app.biu.model.RequestModel.ResponseModel


data class SignInVerifyOTPResponse(
    val success: Boolean?,
    val data: SignInVerifyOTPData?,
    val otp: String?,
)
data class SignInVerifyOTPData
    (val Message:String)
