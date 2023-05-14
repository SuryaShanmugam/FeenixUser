package com.app.feenix.model.response

data class GetLocationResponse(
    val success: Boolean,
    val locations: MutableList<GetLocationData>?,
    val recent_source: MutableList<RecentSourceLocationData>?,
    val recent_destination: MutableList<RecentDestLocationData>?
)

data class GetLocationData(
    val location_id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val is_default: Int,
    val type: String?
)

data class RecentSourceLocationData(
    val s_latitude: Double,
    val s_longitude: Double,
    val s_title: String,
    val s_address: String
)

data class RecentDestLocationData(
    val d_latitude: Double?,
    val d_longitude: Double?,
    val d_title: String?,
    val d_address: String?
)