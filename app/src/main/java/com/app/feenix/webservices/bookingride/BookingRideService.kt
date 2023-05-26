package com.app.feenix.webservices.bookingride

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.app.biu.model.RequestModel.ResponseModel.RideStatusCheckResponse
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.*
import com.app.feenix.model.response.*
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.utils.CustomRideDialog
import com.app.feenix.viewmodel.IBookingRides
import com.app.feenix.viewmodel.IRideStatusCheck
import com.app.feenix.viewmodel.ISendRideRequest
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
        customRideDialog = CustomRideDialog(mContext!!)
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

    // AddLocations
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

    // Get Price Estimation

    private var getPriceEstimationResponse: GetPriceEstimationResponse? = null

    fun getPriceEstimationRide(
        iBookingRides: IBookingRides,
        getPriceEstimationRequest: GetPriceEstimationRequest
    ) {
        IBookingRides = iBookingRides
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.getPriceEstiamtion(
            "XMLHttpRequest",
            myPreference?.userToken!!,
            getPriceEstimationRequest
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetPriceEstimationResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: GetPriceEstimationResponse) {

                    getPriceEstimationResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    getPriceEstimationResponse?.let { IBookingRides?.onGetPriceEstimation(it) }

                }
            })
    }



    // Send Ride Request

    private var sendRideResponse:SendRideResponse? = null
    private var mSendRideRequest:ISendRideRequest? = null
    private var customRideDialog: CustomRideDialog? = null

    fun sendRideRequest(
        iSendRideRequest: ISendRideRequest,
        sendRideRequest: SendRideRequest
    ) {
        customRideDialog?.showDialog(mContext)
        mSendRideRequest = iSendRideRequest
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.SendRideRequest(
            "XMLHttpRequest",
            myPreference?.userToken!!,
            sendRideRequest
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SendRideResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: SendRideResponse) {

                    sendRideResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    customRideDialog?.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    customRideDialog?.hideDialog()
                    sendRideResponse?.let { mSendRideRequest?.onsendRideResponse(it) }
                }
            })
    }

    // Cancel Ride Request

    var cancelRideResponse:CancelRideResponse?=null
    fun CancelRideRequest(
        iSendRideRequest: ISendRideRequest,
        cancelRideRequest: CancelRideRequest
    ) {
        customRideDialog?.showDialog(mContext)
        mSendRideRequest = iSendRideRequest
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.CancelRideRequest(
            "XMLHttpRequest",
            myPreference?.userToken!!,
            cancelRideRequest
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CancelRideResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: CancelRideResponse) {

                    cancelRideResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    customRideDialog?.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    customRideDialog?.hideDialog()
                    cancelRideResponse?.let { mSendRideRequest?.onCancelRideResponse(it) }
                }
            })
    }


    // GetLocations
    var IRideStatusCheck: IRideStatusCheck?=null
    var rideStatusCheckResponse: RideStatusCheckResponse?=null
    fun getRideStatusCheck(iRideStatusCheck: IRideStatusCheck) {
        IRideStatusCheck = iRideStatusCheck
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.getRideStatus(
            "XMLHttpRequest",
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RideStatusCheckResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: RideStatusCheckResponse) {
                    CustomeProgressDialog!!.hideDialog()
                    rideStatusCheckResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    rideStatusCheckResponse?.let { IRideStatusCheck?.onRideStatusCheck(it) }

                }
            })
    }


    // Submit Rating
    fun getProviderRating(iRideStatusCheck: IRideStatusCheck,rateProviderRequest: RateProviderRequest) {
        IRideStatusCheck = iRideStatusCheck
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: BookingRideInterface =
            ApiClient.clientportal.create(BookingRideInterface::class.java)

        val testObservable1 = authService.RateProvider(
            "XMLHttpRequest",
            myPreference?.userToken!!,rateProviderRequest
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RideStatusCheckResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: RideStatusCheckResponse) {
                    CustomeProgressDialog!!.hideDialog()
                    rideStatusCheckResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    Log.e("gete4", "" + e.toString())
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    rideStatusCheckResponse?.let { IRideStatusCheck?.onRatingResponse(it) }

                }
            })
    }
}