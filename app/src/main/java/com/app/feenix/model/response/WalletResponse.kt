package com.app.feenix.model.response

data class WalletResponse(
    val success: Boolean, val wallet_balance: Double,
    val transactions: MutableList<WalletTransactionList>
)

data class WalletTransactionList(
    val id: String?,
    val reference_id: String?,
    val rave_ref_id: String?,
    val flwref: String?,
    val amount: String?,
    val transaction_fee: String?,
    val narration: String?,
    val status: String?,
    val type: String?,
    val created_at: String?
)

data class AddMoneyWalletResponse(
    val success: Boolean?,
    val message: String?,
    val wallet_balance: Boolean?
)