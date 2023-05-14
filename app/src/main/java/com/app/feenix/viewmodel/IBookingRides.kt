package com.app.feenix.viewmodel

import com.app.feenix.model.response.GetLocationResponse


interface IBookingRides {

    fun ongetSavedLocationsHome(getLocationResponse: GetLocationResponse)

}