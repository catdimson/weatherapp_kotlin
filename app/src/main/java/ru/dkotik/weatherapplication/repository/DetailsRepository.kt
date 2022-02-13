package ru.dkotik.weatherapplication.repository

import ru.dkotik.weatherapplication.model.WeatherDTO

interface DetailsRepository {

    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}