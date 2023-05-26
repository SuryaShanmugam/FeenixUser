package com.app.feenix.webservices.Sos

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.*
import com.app.feenix.model.response.*
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.ISos
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SosService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var iSos: ISos? = null
    private var sosContactResponse: SosContactResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun SosService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetEmergencyContacts
    fun getEmergencyContacts(ISos: ISos) {
        iSos = ISos
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: SosInterface =
            ApiClient.clientportal.create(SosInterface::class.java)

        val testObservable1 = authService.getEmergencyContacts(
            "XMLHttpRequest",
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SosContactResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SosContactResponse) {

                    sosContactResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    sosContactResponse?.let { iSos?.ongetEmergencyContactResponse(it) }
                }
            })
    }

    // GetEmergencyContacts

    private var sosSendAlertResponse:SosSendAlertResponse? = null
    fun CreateSendSos(ISos: ISos) {
        iSos = ISos
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: SosInterface =
            ApiClient.clientportal.create(SosInterface::class.java)

        val testObservable1 = authService.SendSOS(
            "XMLHttpRequest",
            myPreference?.userToken!!,myPreference?.CurrentRequestId
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SosSendAlertResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SosSendAlertResponse) {

                    sosSendAlertResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    sosSendAlertResponse?.let { iSos?.onCreateSendSosResponse(it) }
                }
            })
    }
}