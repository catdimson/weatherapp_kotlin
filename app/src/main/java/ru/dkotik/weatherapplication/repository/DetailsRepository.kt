package ru.dkotik.weatherapplication.repository

import okhttp3.Callback
import ru.dkotik.weatherapplication.model.WeatherDTO

interface DetailsRepository {

    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}