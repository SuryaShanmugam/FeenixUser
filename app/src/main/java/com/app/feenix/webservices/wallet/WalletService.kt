package com.app.feenix.webservices.wallet

import android.annotation.SuppressLint
import android.content.Context
import com.app.feenix.app.MyPreference
import com.app.feenix.model.request.AddMoneyWallet
import com.app.feenix.model.response.AddMoneyWalletResponse
import com.app.feenix.model.response.WalletResponse
import com.app.feenix.utils.CustomProgressBar
import com.app.feenix.viewmodel.IWalletData
import com.app.feenix.webservices.ApiClient
import com.app.feenix.webservices.ErrorHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WalletService {


    private var myPreference: MyPreference? = null
    private var CustomeProgressDialog: CustomProgressBar? = null
    private var IWalletData: IWalletData? = null
    private var walletResponse: WalletResponse? = null
    private var addMoneyWalletResponse: AddMoneyWalletResponse? = null
    private var mContext: Context? = null

    @SuppressLint("NotConstructor")
    fun WalletService(context: Context) {
        mContext = context
        myPreference = MyPreference(mContext!!)
        CustomeProgressDialog = CustomProgressBar(mContext!!)
    }

    // GetWalletBalance
    fun getWalletBalance(iWalletData: IWalletData) {
        IWalletData = iWalletData
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: WalletInterface =
            ApiClient.clientportal.create(WalletInterface::class.java)

        val testObservable1 = authService.getWalletdata(
            myPreference?.userToken!!
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WalletResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: WalletResponse) {

                    walletResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    walletResponse?.let { IWalletData?.onWalletResponse(it) }

                }
            })
    }


    fun AddMoney(iWalletData: IWalletData, addMoneyWallet: AddMoneyWallet) {
        IWalletData = iWalletData
        CustomeProgressDialog!!.showDialog(mContext)
        val authService: WalletInterface =
            ApiClient.clientportal.create(WalletInterface::class.java)

        val testObservable1 = authService.AddMoneyWallet(
            myPreference?.userToken!!, addMoneyWallet
        )

        testObservable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AddMoneyWalletResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(loginresponse: AddMoneyWalletResponse) {

                    addMoneyWalletResponse = loginresponse

                }

                override fun onError(e: Throwable) {
                    CustomeProgressDialog!!.hideDialog()
                    ErrorHandler.handle(e.toString())
                }

                override fun onComplete() {
                    CustomeProgressDialog!!.hideDialog()
                    addMoneyWalletResponse?.let { IWalletData?.onAddMoneyWalletResponse(it) }

                }
            })
    }


}