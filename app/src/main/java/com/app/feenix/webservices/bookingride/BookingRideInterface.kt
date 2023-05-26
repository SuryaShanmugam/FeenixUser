package com.app.feenix.webservices.bookingride


import com.app.biu.model.RequestModel.ResponseModel.RideStatusCheckResponse
import com.app.feenix.model.request.*
import com.app.feenix.model.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface BookingRideInterface {

    // Locations
    @GET("locations")
    fun getSavedLocations(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?
    ): Observable<GetLocationResponse>

    @POST("addLocation")
    fun AddLocations(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body addLocation: AddLocation
    ): Observable<GetLocationResponse>

    // Get Service Estimation
    @POST("service_estimate")
    fun getServiceEstiamtion(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body getServiceEstimationRequest: GetServiceEstimationRequest
    ): Observable<GetServiceEstimationResponse>

    // Get Price Estimation Fare
    @POST("estimated/fare")
    fun getPriceEstiamtion(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body getPriceEstimationRequest: GetPriceEstimationRequest
    ): Observable<GetPriceEstimationResponse>


// Send Ride Request
    @POST("send/request")
    fun SendRideRequest(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body sendRideRequest: SendRideRequest
    ): Observable<SendRideResponse>


    // Cancel Ride Request
    @POST("cancel/request")
    fun CancelRideRequest(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body cancelRideRequest: CancelRideRequest
    ): Observable<CancelRideResponse>

// Get Ride Status Check
    @GET("request/check")
    fun getRideStatus(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?
    ): Observable<RideStatusCheckResponse>

    // Rate Provider
    @POST("rate/provider")
    fun RateProvider(
        @Header("X-Requested-With") xmlRequest: String,
        @Header("Authorization") strToken: String?,
        @Body rateProviderRequest: RateProviderRequest
    ): Observable<RideStatusCheckResponse>

}
