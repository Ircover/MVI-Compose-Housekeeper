package com.example.housekeeper.presentation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DateManager {
    private val userFormat = SimpleDateFormat.getDateInstance()

    fun now() = Calendar.getInstance().timeInMillis

    fun formatForUser(millis: Long): String = userFormat.format(Date(millis))
}