package com.app.feenix.webservices.Sos


import com.app.feenix.model.request.*
import com.app.feenix.model.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface SosInterface {

    // Sos Contact List
    @GET("emergency_contacts")
    fun getEmergencyContacts(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?
    ): Observable<SosContactResponse>

    @FormUrlEncoded
    @POST("sendSOSAlert")
    fun SendSOS(
        @Header("X-Requested-With") xmlRequest: String?,
        @Header("Authorization") token: String?,
        @Field("request_id") Requestid: String?
    ): Observable<SosSendAlertResponse>
}
