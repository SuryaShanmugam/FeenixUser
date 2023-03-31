package com.app.feenix.viewmodel

import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse


interface ISignInMobile {

    fun onSignInMobileResponse(signInMobileResponse: SignInMobileResponse)

}