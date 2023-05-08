package com.app.feenix.broadcastreceiver


interface ServiceBroadcastConstants {
    interface BroadcastActions {
        companion object {
            const val INTERNET_CONNECTION_STATUS = "com.app.feenix.internet_connection_status"
            const val LOCATION_CONNECTION_STATUS = "com.app.feenix.location_connection_status"


        }
    }

    interface ServiceTags {
        companion object {
            const val INTERNET_CONNECTIVITY_SERVICE = "com.app.feenix.internetConnectivityService"
            const val PERIODIC_INTERNET_CONNECTIVITY_SERVICE =
                "com.app.feenix.periodicInternetConnectivityService"

        }
    }

    interface ServiceType {
        companion object {
            const val FOREGROUND_SERVICE = 1
        }
    }
}