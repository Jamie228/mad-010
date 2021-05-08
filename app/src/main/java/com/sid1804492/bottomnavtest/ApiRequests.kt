package com.sid1804492.bottomnavtest

import com.sid1804492.bottomnavtest.api.WeatherJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {

    @GET("data/2.5/weather?units=metric")
    fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String = "632dc14467ed8ac5d0df725b305b9c9c"
    ): Call<WeatherJSON>

}