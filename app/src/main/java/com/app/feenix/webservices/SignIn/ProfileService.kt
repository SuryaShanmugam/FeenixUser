package com.app.feenix.webservices.SignIn

import android.annotation.SuppressLint
import android.content.Context
import com.app.feenix.app.MyPreference
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.utils.CustomProgressBar
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

class ProfileService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var iUpdateProfile: IUpdateProfile? = null
    private var updateProfileMobileResponse: UpdateProfileMobileResponse? = null

    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun ProfileService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
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