package com.app.feenix.feature.internet

interface LocationConstants {

    interface BeaconPermissionType {
        companion object {
            const val LOCATION_PERMISSION = 0
            const val LOCATION_SERVICES = 1
            const val BLUETOOTH = 2
        }
    }
}