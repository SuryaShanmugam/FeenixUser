package com.app.feenix.webservices.promotions


import com.app.biu.model.ResponseModel.AddPromocodeResponse
import com.app.biu.model.ResponseModel.PromotionResponse
import com.app.feenix.model.request.*
import io.reactivex.Observable
import retrofit2.http.*

interface PromotionInterface {


    @GET("promocodes")
    fun getPromocodes(
        @Header("Authorization") strToken: String?
    ): Observable<PromotionResponse>

    @POST("promocode/add")
    fun AddPromocodes(
        @Header("Authorization") strToken: String?,
        @Body addPromocode: AddPromocode
    ): Observable<AddPromocodeResponse>
}
