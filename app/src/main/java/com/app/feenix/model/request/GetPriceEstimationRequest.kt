package com.app.feenix.model.request

data class GetPriceEstimationRequest(
    val s_latitude: Double,
    val s_longitude: Double,
    val d_latitude: Double,
    val d_longitude: Double,
    val service_type: Int,
)
