package com.app.feenix.webservices.SignIn


import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse
import com.app.biu.model.RequestModel.ResponseModel.SignInVerifyOTPResponse
import com.app.feenix.model.request.*
import com.app.feenix.model.response.UpdateProfileMobileResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Header("X-Requested-With") request: String?,
        @Header("Authorization") strToken: String?,
        @Body updateProfileMobileRequest: UpdateProfileNameRequest
    ): Observable<UpdateProfileMobileResponse>

    @POST("update/profile")
    fun UpdateProfileEmail(
        @Header("X-Requested-With") request: String?,
        @Header("Authorization") strToken: String?,
        @Body updateProfileEmailRequest: UpdateProfileEmailRequest
    ): Observable<UpdateProfileMobileResponse>

    @Multipart
    @POST("update/profile")
    fun UpdateProfilePic(
        @Header("X-Requested-With") request: String?,
        @Header("Authorization") strToken: String?,
        @Part filePart: MultipartBody.Part
    ): Observable<UpdateProfileMobileResponse>

// update all profile
    @Multipart
    @POST("update/profile")
    fun UpdateProfilewithPic(
        @Header("X-Requested-With") request: String?,
        @Header("Authorization") strToken: String?,
        @Part filePart: MultipartBody.Part,
        @Part ("first_name") first_name:RequestBody,
        @Part ("last_name") last_name:RequestBody,
        @Part ("mobile") mobile:RequestBody,
        @Part ("country_code") country_code:RequestBody,
        @Part ("email") email:RequestBody
): Observable<UpdateProfileMobileResponse>
    @POST("update/profile")
    fun UpdateProfilewithoutPic(
        @Header("X-Requested-With") request: String?,
        @Header("Authorization") strToken: String?,
        @Body updateProfileRequest: UpdateProfileRequest
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
