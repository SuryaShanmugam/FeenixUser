package com.app.feenix.webservices.SignIn

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.biu.model.RequestModel.ResponseModel.*
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.UpdateProfileEmailRequest
import com.app.feenix.model.request.UpdateProfileMobileRequest
import com.app.feenix.model.request.UpdateProfileNameRequest
import com.app.feenix.model.request.VerifyOTPRequest
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.utils.AppSnippet
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.viewmodel.ISignInMobile
import com.app.feenix.viewmodel.ISignInVerifyOtp
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SignInService {

    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var signInMobileResponse: SignInMobileResponse? = null
    private var signInVerifyOTPResponse: SignInVerifyOTPResponse? = null
    private var updateProfileMobileResponse:UpdateProfileMobileResponse? = null
    private var igetProfileData: IGetProfileData? = null
    private var isignInMobile: ISignInMobile? = null
    private var iUpdateProfile: IUpdateProfile? = null
    private var iSignInVerifyOtp: ISignInVerifyOtp? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun SignInService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // Sign IN Mobile
    fun getLoginMobile(iSignInMobile: ISignInMobile, MobileNumber: String, CountryCode: String,email:String) {
        isignInMobile = iSignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        Log.e("CountryCode",CountryCode)
        Log.e("MobileNumber",MobileNumber)

        val testObservable1 = authService.LoginMobileNumber(
            MobileNumber, "android",
            myPreference?.fcmToken!!, AppSnippet.getDeviceId(), CountryCode,"manual",email
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SignInMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SignInMobileResponse) {

                    signInMobileResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                    Log.e("Errorcode", e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    signInMobileResponse?.let { isignInMobile?.onSignInMobileResponse(it) }

                }
            })
    }


    // Sign IN Mobile
    fun getLoginWithEmail(iSignInMobile: ISignInMobile, MobileNumber: String, CountryCode: String,
    Email:String) {
        isignInMobile = iSignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        Log.e("CountryCode",CountryCode)
        Log.e("MobileNumber",MobileNumber)

        val testObservable1 = authService.LoginWithEmail(
            MobileNumber, "android",
            myPreference?.fcmToken!!, AppSnippet.getDeviceId(), CountryCode,"manual",Email)

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SignInMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SignInMobileResponse) {

                    signInMobileResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                    Log.e("Errorcode", e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    signInMobileResponse?.let { isignInMobile?.onSignInMobileResponse(it) }

                }
            })
    }

    // Send OTP
    fun SendOtp(isignInMobile: ISignInVerifyOtp, MobileNumber: String, Content: String) {
        iSignInVerifyOtp = isignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.SendOtp(
            myPreference?.userToken!!, MobileNumber,Content
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SignInVerifyOTPResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SignInVerifyOTPResponse) {

                    signInVerifyOTPResponse = loginresponse

                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    signInVerifyOTPResponse?.let { iSignInVerifyOtp?.onSignInVerifyOtpResponse(it,Content) }

                }
            })
    }


    // Update Phone Number
    fun UpdatePhoneNumber(isignInMobile: ISignInVerifyOtp, MobileNumber: String?, CountryCode: String?) {
        iSignInVerifyOtp = isignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.UpdateProfile(
            myPreference?.userToken!!, UpdateProfileMobileRequest(MobileNumber,CountryCode)
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {

                    updateProfileMobileResponse = loginresponse

                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    updateProfileMobileResponse?.let { iSignInVerifyOtp?.onSignInUpdateMobileNumber(it) }

                }
            })
    }


    //Verify OTP
    fun VerifyOtp(isignInMobile: ISignInVerifyOtp, Otp: String?) {
        iSignInVerifyOtp = isignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.VerfiyOtp(
            myPreference?.userToken!!, VerifyOTPRequest(Otp)
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {
                    CustomeProgressDialog!!.hideDialog()
                   iSignInVerifyOtp?.onSignInVerifyOtp("success")


                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()

                }
            })
    }

    // GetProfile Details
    fun getProfileDetails(iGetProfileData: IGetProfileData) {
        igetProfileData = iGetProfileData
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.getprofiledata(
            myPreference?.userToken!!, "android",AppSnippet.getDeviceId(),myPreference?.fcmToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {

                    updateProfileMobileResponse = loginresponse

                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    updateProfileMobileResponse?.let { igetProfileData?.onGetProfileResponse(it) }

                }
            })
    }



    // Update  UserName details
    fun UpdateUserName(iupdateProfile: IUpdateProfile, firstname: String?, lastname: String?) {
        iUpdateProfile = iupdateProfile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.UpdateProfileName(
            "XMLHttpRequest",
            myPreference?.userToken!!, UpdateProfileNameRequest(firstname, lastname)
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {

                    updateProfileMobileResponse = loginresponse

                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    updateProfileMobileResponse?.let { iUpdateProfile?.onGetProfileNameResponse(it) }

                }
            })
    }

    // Update User Email
    fun UpdateUserEmail(iupdateProfile: IUpdateProfile, Email: String) {
        iUpdateProfile = iupdateProfile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.UpdateProfileEmail(
            "XMLHttpRequest",
            myPreference?.userToken!!, UpdateProfileEmailRequest(Email)
        )
        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {

                    updateProfileMobileResponse = loginresponse

                }
                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    updateProfileMobileResponse?.let { iUpdateProfile?.onGetProfileNameResponse(it) }

                }
            })
    }

    // Update User ProfilePic
    fun UpdateUserPic(iupdateProfile: IUpdateProfile, file: File) {
        iUpdateProfile = iupdateProfile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)


        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "picture",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )

        val testObservable1 = authService.UpdateProfilePic(
            "XMLHttpRequest",
            myPreference?.userToken!!, filePart
        )
        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateProfileMobileResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: UpdateProfileMobileResponse) {

                    updateProfileMobileResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    updateProfileMobileResponse?.let { iUpdateProfile?.onGetProfileNameResponse(it) }

                }
            })
    }

}
