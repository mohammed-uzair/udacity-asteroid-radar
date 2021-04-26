package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.Constants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    /**
     * Function to get current date in default format (MM/dd/yy HH:mm:ss)
     *
     * @return Formatted date string
     */
    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat(
            Constants.API_QUERY_DATE_FORMAT,
            Locale.ENGLISH
        )
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        return dateFormat.format(calendar.time) ?: ""
    }

    fun getDateAfter(days: Int): String {
        val dateFormat: DateFormat = SimpleDateFormat(
            Constants.API_QUERY_DATE_FORMAT,
            Locale.ENGLISH
        )
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.add(Calendar.DATE, days)
        return dateFormat.format(calendar.time) ?: ""
    }

    fun areAsteroidsOutDated(date: String): Boolean {
        val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.ENGLISH)
        val dateToCheck = sdf.parse(date)
        val currentDate = sdf.parse(getCurrentDate())

        if (dateToCheck != null && dateToCheck.before(currentDate)) {
            return true
        }

        return false
    }
}