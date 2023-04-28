package com.app.biu.model.ResponseModel


data class PromotionResponse(
    val success: Boolean?,
    val available_promo: MutableList<PromotionsData>,
    val used_promo: MutableList<PromotionsData>,
)

data class PromotionsData(
    val id: String?,
    val promo_code: String?,
    val discount: String?,
    val expiration: String?,
    val status: String?,
    val fleet_id: String?,
    val type: String?,
    val count_max: String?,
    val driver_contribution: String?,
    val admin_contribution: String?
)


