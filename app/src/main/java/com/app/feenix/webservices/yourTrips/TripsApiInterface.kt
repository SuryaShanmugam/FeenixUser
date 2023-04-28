package com.app.feenix.webservices.yourTrips


import com.app.biu.model.RequestModel.ResponseModel.RideTripResponse
import com.app.feenix.model.request.*
import io.reactivex.Observable
import retrofit2.http.*

interface TripsApiInterface {

    @GET("trips")
    fun getYourTrips(
        @Header("Authorization") strToken: String?,
    ): Observable<RideTripResponse>

}
