package com.example.weather_app_final

data class DataModel(
    val current: Current,
    val location: Location,
    val request: Request,
    val success: Boolean?,

)