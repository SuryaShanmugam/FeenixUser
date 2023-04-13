package com.app.feenix.viewmodel

import com.app.biu.model.RequestModel.ResponseModel.SignInVerifyOTPResponse
import com.app.feenix.model.response.UpdateProfileMobileResponse


interface IUpdateProfile {

    fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse)

}