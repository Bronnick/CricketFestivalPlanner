package com.cricketfest.cricketfestivalplanner.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatDateTime(millis: Long): String = dateTimeFormat.format(Date(millis))
    fun formatDate(millis: Long): String = dateFormat.format(Date(millis))
    fun formatTime(millis: Long): String = timeFormat.format(Date(millis))

    fun isInPast(millis: Long): Boolean = millis < System.currentTimeMillis()
}
