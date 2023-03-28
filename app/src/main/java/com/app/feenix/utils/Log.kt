package com.app.feenix.utils

import android.util.Log


object Log {

    private const val LOCAL_LOG = 0
    private const val AUTH_LOG = 1

    private fun getDateTag() = DateTimeUtils.getNowDateString()

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }


    fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun error(tag: String, message: String) {
        Log.e(tag, message)
    }


    private fun appendLog(text: String, type: Int) {
//        val folder = File(HarmoniApplication.applicationInstance.getExternalFilesDir(null)?.absolutePath, "HarmoniSense")
//        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) , "HarmoniSense")
//        if (!folder.exists()) {
//            folder.mkdirs()
//        }
//
//        val fileName = when(type){
//            LOCAL_LOG -> "LocalLog.txt"
//            AUTH_LOG -> "AuthLog.txt"
//            REMINDER_LOG -> "ReminderLog.txt"
//            BEACON_LOG -> "BeaconLog.txt"
//            BEACON_LOG_2 -> "BeaconLog2.text"
//            BEACON_LOG_3 -> "BeaconLog3.txt"
//            ACTIVITY_LOG -> "ActivityLog.txt"
//            ALARM_LIST_LOG -> "AlarmListLog.txt"
//            SIP_LOG -> "SipLog.txt"
//            ESCALATION_LOG -> "Escalation.txt"
//            COMMON_DEPT -> "CommonDept.txt"
//            OFFLINE_LOG -> "OfflineLog.txt"
//            FCM_LOG -> "FcmLog.txt"
//            DEPT_LOG -> "DeptLog.txt"
//            LOCK_LOG -> "LockLog.txt"
//            SOUND_LOG -> "SoundLog.txt"
//            NOTIFY_LOG -> "NotifyLog.txt"
//            ALARM_ACK_LOG -> "AlarmAckLog.txt"
//            LOGOUT_LOG -> "LogoutLog.txt"
//            INTERNET_LOG -> "InternetLog.txt"
//            BATTERY_LOG -> "BatteryLog.txt"
//            FOREGROUND_SERVICE_LOG -> "ForegroundLog.txt"
//            GENERIC_LOG -> "GenericLog.txt"
//            IN_APP_UPDATE_LOG -> "InAppUpdateLog.txt"
//            else -> "HarmoniSenseLog.txt"
//        }
//        val logFile = File(folder, fileName)
//        if (!logFile.exists()) {
//            try {
//                logFile.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                e.message?.let { debug("LogError", it) }
//            }
//        }
//        try { //BufferedWriter for performance, true to set append to file flag
//            val buf = BufferedWriter(FileWriter(logFile, true))
//            buf.append(text)
//            buf.newLine()
//            buf.newLine()
//            buf.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            e.message?.let { debug("LogError", it) }
//        }
    }
}