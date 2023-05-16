package com.app.feenix.webservices.promotionsAndReferals

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.biu.model.ResponseModel.AddPromocodeResponse
import com.app.biu.model.ResponseModel.PromotionResponse
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.AddPromocode
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.IPromotionData
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PromotionService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var IPromotionData: IPromotionData? = null
    private var promotionResponse: PromotionResponse? = null
    private var addPromocodeResponse: AddPromocodeResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun PromotionService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // Getpromocodes
    fun getPromocodes(ipromotiondata: IPromotionData) {
        IPromotionData = ipromotiondata
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: PromotionInterface =
            ApiClient.clientportal.create(PromotionInterface::class.java)

        val testObservable1 = authService.getPromocodes(
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PromotionResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: PromotionResponse) {

                    promotionResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    promotionResponse?.let { IPromotionData?.ongetPromotionCodes(it) }

                }
            })
    }

    // Addpromocodes
    fun AddPromocodes(ipromotiondata: IPromotionData, addPromocode: AddPromocode) {
        IPromotionData = ipromotiondata
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: PromotionInterface =
            ApiClient.clientportal.create(PromotionInterface::class.java)

        val testObservable1 = authService.AddPromocodes(
            myPreference?.userToken!!,
            addPromocode
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AddPromocodeResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: AddPromocodeResponse) {

                    addPromocodeResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    Log.e("edwe", "" + e.toString())
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    addPromocodeResponse?.let { IPromotionData?.onAddPromotionCodeRes(it) }

                }
            })
    }


}