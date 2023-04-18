package com.app.feenix.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun tripsDateformat(
        currentFormat: String,
        requiredFormat: String,
        dateString: String?
    ): String? {
        var result = ""
        val formatterOld = SimpleDateFormat(currentFormat, Locale.getDefault())
        val formatterNew = SimpleDateFormat(requiredFormat, Locale.getDefault())
        var date: Date? = null
        try {
            date = formatterOld.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date != null) {
            result = formatterNew.format(date)
        }
        return result
    }


}