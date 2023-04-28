package com.app.feenix.webservices.yourTrips

import android.annotation.SuppressLint
import android.content.Context
import com.app.biu.model.RequestModel.ResponseModel.RideTripResponse
import com.app.feenix.app.MyPreference
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.utils.Log
import com.app.feenix.viewmodel.IYourTripsData
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class YourTripService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null

    private var iYourTripsData: IYourTripsData? = null
    private var rideTripResponse: RideTripResponse? = null

    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun YourTripService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetTrip List
    fun getRideTrips(iGetProfileData: IYourTripsData) {
        iYourTripsData = iGetProfileData
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: TripsApiInterface =
            ApiClient.clientportal.create(TripsApiInterface::class.java)

        val testObservable1 = authService.getYourTrips(
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RideTripResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: RideTripResponse) {

                    rideTripResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    Log.error("dfsferwsd", e.toString())
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    rideTripResponse?.let { iYourTripsData?.onRideTripResponse(it) }

                }
            })
    }


}