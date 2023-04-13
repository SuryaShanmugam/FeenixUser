package com.app.feenix.viewmodel

import com.app.biu.model.RequestModel.ResponseModel.SignInVerifyOTPResponse
import com.app.feenix.model.response.UpdateProfileMobileResponse


interface ISignInVerifyOtp {

    fun onSignInVerifyOtpResponse(signInVerifyOTPResponse: SignInVerifyOTPResponse,type:String)
    fun onSignInUpdateMobileNumber(updateProfileMobileResponse: UpdateProfileMobileResponse)
    fun onSignInVerifyOtp(success: String)

}