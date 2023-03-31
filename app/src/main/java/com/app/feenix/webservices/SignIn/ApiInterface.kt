package com.app.feenix.webservices.SignIn


import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ApiInterface {

    //Auth Service
    @POST("login")
    @FormUrlEncoded
    fun LoginMobileNumber(
        @Field("mobile") mobile: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
        @Field("country_code") country_code: String,
        @Field("login_by") login_by: String
    ): Observable<SignInMobileResponse>

}
