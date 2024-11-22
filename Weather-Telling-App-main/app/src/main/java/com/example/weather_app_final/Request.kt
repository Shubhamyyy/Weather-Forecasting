package com.example.weather_app_final

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)