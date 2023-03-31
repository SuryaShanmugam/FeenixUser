package com.app.feenix.webservices

/**
 * This class contains constant values which are used while API response handling
 */
class NetworkConstants {
    companion object {
        const val HEADER_DATE_KEY = "Date"

        const val UNAUTHORISED = 401
        const val FORBIDDEN = 403
        const val BAD_REQUEST = 400
        const val REQUEST_TIME_OUT = 408
        const val INTERNAL_SERVER_ERROR = 500

        //Custom error codes
        const val GENERAL_ERROR_CODE = 999
        const val TIME_OUT = 1000
        const val JSON_PARSING = 1001
        const val SERVER_CONNECTION_FAILED = 1002
        const val SERVICE_UNAVAILABLE = 1003
        const val UNAUTHORIZED_VERIFICATION_FAILED = 1004
    }
}