package ru.dkotik.weatherapplication.repository

import ru.dkotik.weatherapplication.model.Weather

interface Repository {

    fun getWeatherFromServer(): Weather

    fun getWeatherFromLocalStorageRus(): List<Weather>

    fun getWeatherFromLocalStorageWorld(): List<Weather>
}