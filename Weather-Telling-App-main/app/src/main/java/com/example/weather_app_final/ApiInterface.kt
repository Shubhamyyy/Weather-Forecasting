package com.example.weather_app_final

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("current")
    fun getWeatherData(
        @Query("access_key") apiKey: String,
        @Query("query") location: String
    ): Call<DataModel>
}
