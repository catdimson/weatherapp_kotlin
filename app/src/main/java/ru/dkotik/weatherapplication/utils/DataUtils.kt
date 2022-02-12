package ru.dkotik.weatherapplication.utils

import ru.dkotik.weatherapplication.model.FactDTO
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.model.WeatherDTO
import ru.dkotik.weatherapplication.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feelLike!!,
        fact.condition!!))
}