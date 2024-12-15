package com.dicoding.wanmuhtd.storyapp.helper

import android.location.Geocoder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import android.content.Context
import android.location.Address


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

fun getCityNameFromCoordinates(lat: Double, lon: Double, context: Context): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addressList: List<Address?>? = geocoder.getFromLocation(lat, lon, 1)
        if (addressList!!.isNotEmpty()) {
            val address = addressList[0]
            val city = address?.subAdminArea
            val country = address?.countryName
            "$city, $country"
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}