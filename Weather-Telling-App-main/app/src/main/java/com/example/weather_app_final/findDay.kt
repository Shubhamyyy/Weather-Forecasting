package com.example.weather_app_final

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
fun findDayOfWeek(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date, formatter)
    val dayOfWeek = localDate.dayOfWeek
    return dayOfWeek.toString()
}
