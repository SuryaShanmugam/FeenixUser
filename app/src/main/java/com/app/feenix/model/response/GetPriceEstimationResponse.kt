package com.app.feenix.model.response

data class GetPriceEstimationResponse(
    val success: Boolean,
    val estimated_fare: Double?,
    val distance: Int,
    val distance_price: Double,
    val time: String,
    val time_price: Double,
    val tax_price: Double,
    val wallet_balance: Double,
    val base_price: Double,
    val discount: Double,

    )

