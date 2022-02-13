package ru.dkotik.weatherapplication.repository

import ru.dkotik.weatherapplication.model.Weather

interface LocalRepository {

    fun getAllHistory(): List<Weather>

    fun saveEntity(weather: Weather)

}