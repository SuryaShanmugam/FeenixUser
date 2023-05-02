package com.app.feenix.viewmodel

import com.app.biu.model.ResponseModel.NotificationResponse


interface INotificationData {

    fun ongetNotifications(notificationResponse: NotificationResponse)

}