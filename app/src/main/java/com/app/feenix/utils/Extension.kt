@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package cbs.com.bmr.Utilities


import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

fun Any.toast(context: Context) {
    Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show()
}

fun String?.isNa(): String {
    return if (this.isNullOrBlank()) "-" else this.orEmpty()
}

fun Double?.toPrice(): String? {
    if (this == null || this == 0.toDouble()) return null
    return String.format(Locale.getDefault(), "\u20B9 %s", DecimalFormat("##,##,##0").format(this))
}

fun Double?.toDiscountPrice(): String? {
    if (this == null || this == 0.toDouble()) return null
    return String.format(
        Locale.getDefault(),
        "\u20B9 %s (-)",
        DecimalFormat("##,##,##0").format(this)
    )
}

fun Int?.toOffer(): String? {
    if (this == null || this == 0) return null
    return String.format(Locale.getDefault(), "%d%% off", this)
}


fun Float.toRating(): String? {
    return String.format(Locale.getDefault(), "%.1f", this)
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun String.toDateTime(): String? {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        Locale.getDefault()
    )
    try {
        val date = dateFormat.parse(this)
        val simpleDateFormat = SimpleDateFormat(
            "EEE, d MMM''yy",
            Locale.getDefault()
        )
        return simpleDateFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toRelativeTime(): String? {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        Locale.getDefault()
    )
    try {
        val date = dateFormat.parse(this)
        return DateUtils.getRelativeTimeSpanString(
            date.time,
            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

