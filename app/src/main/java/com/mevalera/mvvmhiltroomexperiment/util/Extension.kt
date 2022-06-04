package com.mevalera.mvvmhiltroomexperiment.util

import android.text.format.DateUtils
import android.view.View
import java.text.SimpleDateFormat
import java.util.*


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun String.formatDateTime(time: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    val formatSdf = sdf.parse(time)
    return DateUtils.getRelativeTimeSpanString(formatSdf?.time ?: 0).toString()
}