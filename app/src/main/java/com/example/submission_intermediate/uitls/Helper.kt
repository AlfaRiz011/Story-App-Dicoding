package com.example.submission_intermediate.uitls

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Helper {

    fun formatCreatedAt(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(timestamp)
        date?.let {
            val now = System.currentTimeMillis()
            val timeAgo = DateUtils.getRelativeTimeSpanString(date.time, now, DateUtils.MINUTE_IN_MILLIS)
            return "diupload $timeAgo"
        }
        return ""
    }

}