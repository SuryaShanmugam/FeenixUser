package com.app.feenix.viewmodel

import com.app.feenix.model.response.SendRideResponse


interface ISendRideRequest {

    fun onsendRideResponse(sendRideResponse: SendRideResponse)

}