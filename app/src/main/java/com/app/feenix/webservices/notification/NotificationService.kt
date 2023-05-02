package com.app.feenix.webservices.notification

import android.annotation.SuppressLint
import android.content.Context
import com.app.biu.model.ResponseModel.NotificationResponse
import com.app.feenix.app.MyPreference
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.INotificationData
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var INotificationData: INotificationData? = null
    private var notificationResponse: NotificationResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun NotificationService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetNotifications
    fun getNotification(iNotificationData: INotificationData) {
        INotificationData = iNotificationData
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: NotificationInterface =
            ApiClient.clientportal.create(NotificationInterface::class.java)

        val testObservable1 = authService.getNotification(
            "XMLHttpRequest",
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<NotificationResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: NotificationResponse) {

                    notificationResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    notificationResponse?.let { INotificationData?.ongetNotifications(it) }

                }
            })
    }


}