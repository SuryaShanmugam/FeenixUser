package com.app.feenix.viewmodel

import com.app.feenix.model.response.AddMoneyWalletResponse
import com.app.feenix.model.response.WalletResponse


interface IWalletData {

    fun onWalletResponse(walletResponse: WalletResponse)
    fun onAddMoneyWalletResponse(addMoneyWalletResponse: AddMoneyWalletResponse)

}