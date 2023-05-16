package com.app.feenix.webservices.bookingride


import com.app.feenix.model.request.*
import com.app.feenix.model.response.GetLocationResponse
import com.app.feenix.model.response.GetPriceEstimationResponse
import com.app.feenix.model.response.GetServiceEstimationResponse
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

}
