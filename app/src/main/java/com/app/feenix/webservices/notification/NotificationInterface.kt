package com.app.feenix.webservices.notification


import com.app.biu.model.ResponseModel.NotificationResponse
import com.app.feenix.model.request.*
import io.reactivex.Observable
import retrofit2.http.*

interface NotificationInterface {


    @GET("notifications")
    fun getNotification(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?
    ): Observable<NotificationResponse>

}
