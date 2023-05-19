package com.app.feenix.model.request

data class CancelRideRequest(
    val request_id: String?,
    val reason: String?
)
