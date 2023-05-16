package com.app.feenix.app


object Constant {
    const val INTERNET_CONNECTION_STATUS = "internetConnectionStatus"
    const val IS_SERVICE_START = "isServiceStart"
    const val ACTIVITY_STARTED_SERVICE = "activityStartedService"

    var IS_BOOKING_FLOW_STARTED = 0


    var CURRENT_LAT = 0.0
    var CURRENT_LNG = 0.0

    // Destination
    var DEST_LNG = 0.0
    var DEST_LAT = 0.0
    var DEST_ADDRESS = "DestinationAddress"
    var DEST_TITLE = "DestinationTitle"

    // Source
    var SOURCE_LNG = 0.0
    var SOURCE_LAT = 0.0
    var SOURCE_ADDRESS = "SourceAddress"
    var SOURCE_TITLE = "SourceTitle"

    //    ServiceType
    var SERVICE_TYPE = 0
    var ESTIMATED_FARE = 0.0
    var DISTANCE = "Distance"
    var DISCOUNT = 0.0
    var DISTANCE_PRICE = "DistancePrice"
    var TIME = "Time"
    var TIME_PRICE = "TimePrice"
    var TAX_PRICE = "TaxPrice"
    var BASE_PRICE = "BasePrice"
    var WALLET_BAL = "walletbalance"


//    PaymentMode

    var PAYMENT_MODE = "paymentMode"
    var IS_SELECTED_WALLET = 0

}