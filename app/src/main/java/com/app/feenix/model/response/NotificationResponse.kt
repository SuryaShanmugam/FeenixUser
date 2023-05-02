package com.app.biu.model.ResponseModel


data class NotificationResponse(
    val success: Boolean?,
    val notifications: MutableList<NotificationData>
)

data class NotificationData(
    val id: Int?,
    val user_id: String?,
    val driver_id: String?,
    val request_id: String?,
    val message: String?,
    val title: String?,
    val status: String?,
    val created_at: String?
)
