package com.app.feenix.viewmodel

import com.app.biu.model.RequestModel.ResponseModel.RideStatusCheckResponse


interface IRideStatusCheck {

    fun onRideStatusCheck(rideStatusCheckResponse: RideStatusCheckResponse)
    fun onRatingResponse(rideStatusCheckResponse: RideStatusCheckResponse)

}