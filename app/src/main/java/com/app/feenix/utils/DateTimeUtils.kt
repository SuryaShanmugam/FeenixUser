package com.app.feenix.utils

import android.os.Build
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@Suppress("unused")
object DateTimeUtils {
    const val SERVER_HEADER_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'"

    //    const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val MESSAGE_CREATED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val ALARM_REMINDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val SIP_CALL_DATE_FORMAT = "yyyy-MM-dd HH:mm"
    const val LIVE_STREAM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val OFFLINE_ALARM_REQUEST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val GENERAL_NOTIFICATION_FORMAT = "MMM dd, hh:mm aa"
    private const val UTC_TIME_ZONE = "UTC"
    private const val GENERAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    var serverTimeInMillis: Long = 0
    var serverTimeInUtcMillis: Long = 0
    var systemTimeInMillis: Long = 0


    fun getNowDateInMillis(): Long {
        val nowDate = getDeviceDateInMillis()
        return if (serverTimeInMillis == 0L) {
            Calendar.getInstance().timeInMillis
        } else {
            val timeDiff = nowDate - systemTimeInMillis
            var serverMillis = serverTimeInMillis
            if (timeDiff > 0) {
                serverMillis += timeDiff
            }
            serverMillis
        }
    }

    fun getNowDateUtcInMillis(): Long {
        val nowDate = getDeviceDateInMillis()
        return if (serverTimeInUtcMillis == 0L) {
            Calendar.getInstance(getUtcTimeZone()).timeInMillis
        } else {
            val timeDiff = nowDate - systemTimeInMillis
            var serverMillis = serverTimeInUtcMillis
            if (timeDiff > 0) {
                serverMillis += timeDiff
            }
            return serverMillis
        }
    }

    fun getNowDate(): Date = Date(getNowDateInMillis())

    fun getDeviceDateInMillis(): Long = Date().time

    fun getNowDateUtc(): Date {
        return Date(getNowDateUtcInMillis())
    }

    fun getUtcTimeZone(): TimeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)

    fun getNowDateString(): String {
        val dateString =
            getDateStringFromDate(getNowDate(), GENERAL_DATE_FORMAT, TimeZone.getDefault())
        dateString ?: return ""
        return dateString
    }

    fun getNowDateStringUtc(): String {
        val dateString = getUtcDateStringFromDate(getNowDateUtc(), GENERAL_DATE_FORMAT)
        dateString ?: return ""
        return dateString
    }

    fun getNowDateStringUtc(neededFormat: String): String {
        val dateString = getUtcDateStringFromDate(getNowDateUtc(), neededFormat)
        dateString ?: return ""
        return dateString
    }

    fun getMillisFromDateStringUsingInstant(date: String): Long {
        val formattedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Date.from(Instant.parse(date))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return formattedDate.time
    }

    fun getFormattedDateString(
        date: String,
        neededFormat: String,
        currentFormat: String
    ): String? {
        getDateFromDateString(date, currentFormat, getUtcTimeZone())?.let {
            return SimpleDateFormat(
                neededFormat,
                Locale.ENGLISH
            ).format(it)
        }
        return null
    }

    fun getFormattedTimeString(
        timeInMills: Long,
        neededFormat: String
    ): String? {
        return SimpleDateFormat(neededFormat, Locale.ENGLISH).format(timeInMills)
    }

    fun getFormattedTimeString(
        time: String,
        currentFormat: String,
        neededFormat: String
    ): String? {
        getDateFromDateString(time, currentFormat, null)?.let {
            return SimpleDateFormat(
                neededFormat,
                Locale.ENGLISH
            ).format(it)
        }
        return null
    }

    fun getDateFromDateString(
        dateString: String,
        format: String,
        timeZone: TimeZone?
    ): Date? {
        val simpleDateFormat: DateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        timeZone?.let { simpleDateFormat.timeZone = it }
        try {
            return simpleDateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun convertToDateTimeZoneMillis(dateInMillis: Long, timeZone: TimeZone): Long {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = dateInMillis
        return calendar.timeInMillis
    }

    private fun getTimeZone(): String? {
        val gmtSimpleDateFormat: DateFormat = SimpleDateFormat("Z", Locale.ENGLISH)
        gmtSimpleDateFormat.timeZone = TimeZone.getDefault()
        gmtSimpleDateFormat.format(Calendar.getInstance().time)
        return gmtSimpleDateFormat.format(Calendar.getInstance().time)
    }

    fun getUtcDateStringFromDate(
        date: Date,
        format: String,
        timeZone: String?
    ): String? {
        val gmtSimpleDateFormat: DateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        timeZone?.let { gmtSimpleDateFormat.timeZone = TimeZone.getTimeZone(it) }
        return gmtSimpleDateFormat.format(date)
    }

    fun getUtcDateStringFromDate(date: Date, format: String): String? {
        val simpleDateFormat: DateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        return simpleDateFormat.format(date)
    }

    fun getDateStringFromDate(
        date: Date,
        neededFormat: String,
        timeZone: TimeZone?
    ): String? {
        val gmtSimpleDateFormat: DateFormat = SimpleDateFormat(neededFormat, Locale.ENGLISH)
        timeZone?.let {
            gmtSimpleDateFormat.timeZone = timeZone
        }
        return gmtSimpleDateFormat.format(date)
    }

    fun getMillisecondsFromDateString(
        dateString: String,
        currentFormat: String,
        timeZone: TimeZone?
    ): Long? {
        val date = getDateFromDateString(dateString, currentFormat, timeZone)
        return date?.time
    }
}