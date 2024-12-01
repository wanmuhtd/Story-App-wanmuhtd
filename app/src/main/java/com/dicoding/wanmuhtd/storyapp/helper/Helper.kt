package com.dicoding.wanmuhtd.storyapp.helper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


fun String.withDateFormat(): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = originalFormat.parse(this)
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        formatter.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}