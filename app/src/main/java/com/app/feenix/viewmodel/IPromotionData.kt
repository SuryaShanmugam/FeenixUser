package com.app.feenix.viewmodel

import com.app.biu.model.ResponseModel.AddPromocodeResponse
import com.app.biu.model.ResponseModel.PromotionResponse


interface IPromotionData {

    fun ongetPromotionCodes(promotionResponse: PromotionResponse)
    fun onAddPromotionCodeRes(addPromocodeResponse: AddPromocodeResponse)

}