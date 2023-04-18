package com.app.feenix.viewmodel

import com.app.biu.model.RequestModel.ResponseModel.RideTripResponse


interface IYourTripsData {

    fun onRideTripResponse(rideTripResponse: RideTripResponse)

}