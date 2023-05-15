package com.app.feenix.model.response

data class GetServiceEstimationResponse(
    val success: Boolean,
    val new_delivery: Int,
    val services: MutableList<ServiceEstimationData>?
)

data class ServiceEstimationData(
    val id: Int?,
    val name: String?,
    val provider_name: String?,
    val image: String?,
    val fixed: String?,
    val minimum_fare: String?,
    val base_radius: String?,
    val price: String?,
    val time: String?,
    val drivercommission: String?,
    val commission: String?,
    val calculator: String?,
    val description: String?,
    val type: Int?,
    val is_delivery: Int?,
    val base_price: Double?,
    val eta: Int?,
    val time_price: Double?,
    val distance_price: Double?,
    val distance: String?,
    val estimated_fare: String?,
    var isSelected: Boolean
)
