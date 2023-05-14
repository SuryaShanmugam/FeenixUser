package com.app.feenix.model.request

data class AddLocation(
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val title: String?,
    val type: String?,
)
