package com.app.feenix.webservices.SignIn


import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse
import com.app.biu.model.RequestModel.ResponseModel.SignInVerifyOTPResponse
import com.app.feenix.model.request.UpdateProfileEmailRequest
import com.app.feenix.model.request.UpdateProfileMobileRequest
import com.app.feenix.model.request.UpdateProfileNameRequest
import com.app.feenix.model.request.VerifyOTPRequest
import com.app.feenix.model.response.UpdateProfileMobileResponse
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
        @Field("login_by") login_by: String,
        @Field("email") email: String
    ): Observable<SignInMobileResponse>
    @POST("login")
    @FormUrlEncoded
    fun LoginWithEmail(
        @Field("mobile") mobile: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
        @Field("device_id") device_id: String,
        @Field("country_code") country_code: String,
        @Field("login_by") login_by: String,
        @Field("email") email: String
    ): Observable<SignInMobileResponse>

    // Verify OTP
    @FormUrlEncoded
    @POST("sendOTP")
    fun SendOtp(
        @Header("Authorization") strToken: String?,
        @Field("to") mobilenumber: String,
        @Field("content") content: String
    ): Observable<SignInVerifyOTPResponse>

    // Update Profile

    @POST("update/profile")
    fun UpdateProfile(
        @Header("Authorization") strToken: String?,
        @Body updateProfileMobileRequest: UpdateProfileMobileRequest
    ): Observable<UpdateProfileMobileResponse>

    @POST("update/profile")
    fun UpdateProfileName(
        @Header("Authorization") strToken: String?,
        @Body updateProfileMobileRequest: UpdateProfileNameRequest
    ): Observable<UpdateProfileMobileResponse>
    @POST("update/profile")
    fun UpdateProfileEmail(
        @Header("Authorization") strToken: String?,
        @Body updateProfileEmailRequest: UpdateProfileEmailRequest
    ): Observable<UpdateProfileMobileResponse>

    // Get Profile

    @GET("details")
    fun getprofiledata(
        @Header("Authorization") strToken: String?,
        @Query("device_type") device_type: String,
        @Query("device_id") device_id: String,
        @Query("device_token") device_token: String

    ): Observable<UpdateProfileMobileResponse>

    // Verify OTP

    @POST("update/profile")
    fun VerfiyOtp(
        @Header("Authorization") strToken: String?,
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Observable<UpdateProfileMobileResponse>

}
