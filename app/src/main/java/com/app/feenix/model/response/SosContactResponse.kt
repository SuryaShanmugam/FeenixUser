package com.app.feenix.model.response

data class SosContactResponse(
    val success: Boolean,
    val contacts: MutableList<SosContactData>,

)
data class SosContactData(
    val id:Int?,
    val user_id:Int?,
    val first_name:String?,
    val last_name:String?,
    val email:String?,
    val picture:String?,
    val mobile:String?,
    val country_code:String?
)
