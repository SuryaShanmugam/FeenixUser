package com.app.feenix.webservices.promotionsAndReferals

import android.annotation.SuppressLint
import android.content.Context
import com.app.biu.model.ResponseModel.ReferalResponse
import com.app.feenix.app.MyPreference
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.IReferalData
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ReferalService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var IPromotionData: IReferalData? = null
    private var promotionResponse: ReferalResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun ReferalService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetReferalCodes
    fun getReferals(ipromotiondata: IReferalData) {
        IPromotionData = ipromotiondata
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: PromotionInterface =
            ApiClient.clientportal.create(PromotionInterface::class.java)

        val testObservable1 = authService.getReferals(
            "XMLHttpRequest",
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ReferalResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: ReferalResponse) {

                    promotionResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    promotionResponse?.let { IPromotionData?.ongetReferalResponse(it) }

                }
            })
    }


}