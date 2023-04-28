package com.app.feenix.model.request

data class AddMoneyWallet(
    val amount: String?, val network: String?,
    val payment_mode: String?, val payToken: String?,
    val mobile: String?
)