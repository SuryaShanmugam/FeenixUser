package com.app.feenix.app

import com.app.feenix.R
import com.app.feenix.utils.LocaleContextWrapper
import com.app.feenix.webservices.NetworkConstants

data class ErrorBody(
    var errorCode: Int = NetworkConstants.GENERAL_ERROR_CODE,
    var errorMessage: String = LocaleContextWrapper.getLocaleString(R.string.general_network_error)
)