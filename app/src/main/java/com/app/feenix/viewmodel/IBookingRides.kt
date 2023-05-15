package com.app.feenix.viewmodel

import com.app.feenix.model.response.GetLocationResponse
import com.app.feenix.model.response.GetServiceEstimationResponse


interface IBookingRides {

    fun ongetSavedLocationsHome(getLocationResponse: GetLocationResponse)
    fun onGetServiceTypeEstimation(getServiceEstimationResponse: GetServiceEstimationResponse)

}