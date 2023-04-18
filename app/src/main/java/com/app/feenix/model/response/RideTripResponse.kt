package com.app.biu.model.RequestModel.ResponseModel


data class RideTripResponse(
    val success: Boolean?,
    val data: MutableList<RideTripResponseData>
)

data class RideTripResponseData(
    val id: String?,
    val booking_id: String?,
    val user_id: String?,
    val provider_id: String?,
    val current_provider_id: String?,
    val service_type_id: String?,
    val status: String?,
    val eta: String?,
    val cancelled_by: String?,
    val cancelled_reason: String?,
    val payment_mode: String?,
    val paid: String?,
    val driver_payout: String?,
    val driver_payment_id: String?,
    val s_address: String?,
    val s_latitude: String?,
    val s_longitude: String?,
    val d_address: String?,
    val d_latitude: String?,
    val d_longitude: String?,
    val assigned_at: String?,
    val accepted_at: String?,
    val finished_at: String?,
    val use_wallet: String?,
    val user_rated: String?,
    val provider_rated: String?,
    val receiver_name: String?,
    val receiver_mobile: String?,
    val pickup_instruction: String?,
    val delivery_instruction: String?,
    val package_type: String?,
    val package_details: String?,
    val confirmation_code: String?,
    val fleet_id: String?,
    val notification: String?,
    val total: Double?,
    val s_title: String?,
    val d_title: String?,
    val reroute: String?,
    val estimated_fare: String?,
    val distance_price: String?,
    val time: String?,
    val time_price: String?,
    val tax_price: String?,
    val base_price: String?,
    val wallet_balance: String?,
    val payment_image: String?,
//    val payment: String?,
    val service_type: RideTripServiceType?,
    val user: RideTripUser?,
    val provider: RideTripProvider?,
    val provider_profiles: RideTripProviderProfiles?,
//    val rating: String?
)

data class RideTripProviderProfiles(
    val car_registration: String?,
    val provider_id: String?
)

data class RideTripServiceType(
    val id: String?,
    val name: String?,
    val provider_name: String?,
    val image: String?,
    val fixed: String?,
    val minimum_fare: String?,
    val base_radius: String?,
    val price: String?,
    val time: String?,
    val commission: String?,
    val drivercommission: String?,
    val calculator: String?,
    val description: String?,
    val status: String?,
    val type: String?,
    val is_delivery: String,

    )

data class RideTripUser(
    val id: String?,
    val first_name: String?,
    val last_name: String?,
    val payment_mode: String?,
    val email: String?,
    val picture: String?,
    val device_token: String?,
    val device_id: String?,
    val device_type: String?,
    val login_by: String?,
    val referal: String?,
    val mobile: String?,
    val country_code: String?,
    val archive: String?,
    val fleet: String?

)

data class RideTripProvider(
    val id: String?,
    val first_name: String?,
    val last_name: String?,
    val payment_mode: String?,
    val email: String?,
    val avatar: String?,
    val status: String?,
    val wallet_balance: String?,
    val rating_count: String?,
    val referal: String?,
    val rating: String?
)
