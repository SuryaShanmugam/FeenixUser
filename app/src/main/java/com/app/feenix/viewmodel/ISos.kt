package com.app.feenix.viewmodel

import com.app.feenix.model.response.SosContactResponse
import com.app.feenix.model.response.SosSendAlertResponse


interface ISos {

    fun ongetEmergencyContactResponse(sosContactResponse: SosContactResponse)
    fun onCreateSendSosResponse(sosSendAlertResponse: SosSendAlertResponse)

}