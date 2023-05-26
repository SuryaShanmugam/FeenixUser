package com.app.feenix.utils

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import java.text.DateFormat
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

    fun getPaymentDate(createdOn: String?): String? {
        if (createdOn != null) {
            val dateFormat: DateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            )
            try {
                val date = dateFormat.parse(createdOn)
                val simpleDateFormat = SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )
                return simpleDateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return createdOn
    }


    open fun polyLineAnimator(): ValueAnimator? {
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 2000
        return valueAnimator
    }

    open fun cabAnimator(): ValueAnimator? {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 3000
        return valueAnimator
    }
    fun getstartTime(createdOn: String?): String? {
        if (createdOn != null) {
            val dateFormat: DateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            )
            try {
                val date = dateFormat.parse(createdOn)
                val simpleDateFormat = SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                )
                return simpleDateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return createdOn
    }

}