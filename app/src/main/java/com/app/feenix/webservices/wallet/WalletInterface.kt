package com.app.feenix.webservices.wallet


import com.app.feenix.model.request.*
import com.app.feenix.model.response.AddMoneyWalletResponse
import com.app.feenix.model.response.WalletResponse
import io.reactivex.Observable
import retrofit2.http.*

interface WalletInterface {


    @GET("wallet_balance_sp")
    fun getWalletdata(
        @Header("Authorization") strToken: String?
    ): Observable<WalletResponse>


    @POST("add_money_sp")
    fun AddMoneyWallet(
        @Header("Authorization") strToken: String?,
        @Body addMoneyWallet: AddMoneyWallet

    ): Observable<AddMoneyWalletResponse>

}
