package com.app.feenix.model.response

data class SendRideResponse(
    val error: Boolean?,
    val success: Boolean?,
    val message: String?,
    val request_id: String?,
    val current_provider: String?)

data class CancelRideResponse(
    val success: Boolean?,
    val message: String?)