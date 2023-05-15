package com.app.feenix.webservices.bookingride

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.AddLocation
import com.app.feenix.model.request.GetServiceEstimationRequest
import com.app.feenix.model.response.GetLocationResponse
import com.app.feenix.model.response.GetServiceEstimationResponse
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.IBookingRides
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookingRideService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var IBookingRides: IBookingRides? = null
    private var getLocationResponse: GetLocationResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun BookingRideService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetLocations
    fun getSavedLocations(iBookingRides: IBookingRides) {
        IBookingRides = iBookingRides
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.getSavedLocations(
            "XMLHttpRequest",
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetLocationResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: GetLocationResponse) {

                    getLocationResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    getLocationResponse?.let { IBookingRides?.ongetSavedLocationsHome(it) }

                }
            })
    }

    // GetLocations
    fun AddLocation(iBookingRides: IBookingRides, addLocation: AddLocation) {
        IBookingRides = iBookingRides
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.AddLocations(
            "XMLHttpRequest",
            myPreference?.userToken!!,
            addLocation
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetLocationResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: GetLocationResponse) {

                    getLocationResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    getLocationResponse?.let { IBookingRides?.ongetSavedLocationsHome(it) }

                }
            })
    }

    // Get ServiceType Estimation
    private var getServiceEstimationResponse: GetServiceEstimationResponse? = null

    fun getServiceTypeEstimation(
        iBookingRides: IBookingRides,
        getServiceEstimationRequest: GetServiceEstimationRequest
    ) {
        IBookingRides = iBookingRides
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.getServiceEstiamtion(
            "XMLHttpRequest",
            myPreference?.userToken!!,
            getServiceEstimationRequest
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetServiceEstimationResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: GetServiceEstimationResponse) {

                    getServiceEstimationResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    getServiceEstimationResponse?.let { IBookingRides?.onGetServiceTypeEstimation(it) }

                }
            })
    }

}