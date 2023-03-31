package com.app.feenix.webservices.SignIn

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.biu.model.RequestModel.ResponseModel.*
import com.app.feenix.app.MyPreference
import com.app.feenix.utils.AppSnippet
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.ISignInMobile
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SignInService {

    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var signInMobileResponse: SignInMobileResponse? = null
    private var isignInMobile: ISignInMobile? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun SignInService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }


    fun getLoginMobile(iSignInMobile: ISignInMobile, MobileNumber: String, CountryCode: String) {
        isignInMobile = iSignInMobile
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: ApiInterface = ApiClient.clientportal.create(ApiInterface::class.java)

        val testObservable1 = authService.LoginMobileNumber(
            MobileNumber, "android",
            myPreference?.fcmToken!!, AppSnippet.getDeviceId(), "manual", CountryCode
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


}
