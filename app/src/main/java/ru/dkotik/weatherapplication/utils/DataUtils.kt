package ru.dkotik.weatherapplication.utils

import ru.dkotik.weatherapplication.model.*
import ru.dkotik.weatherapplication.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feelLike!!,
        fact.condition!!))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.city, weather.temperature, weather.condition)
}

fun changeConditionEngToRus(condition: String): String {
    return when(condition.lowercase()) {
        "clear" -> "Ясно"
        "partly-cloudy" -> "Малооблачно"
        "cloudy" -> "Облачно с прояснениями"
        "overcast" -> "Пасмурно"
        "drizzle" -> "Морось"
        "light-rain" -> "Небольшой дождь"
        "rain" -> "Дождь"
        "moderate-rain" -> "Умеренно сильный дождь"
        "heavy-rain" -> "Сильный дождь"
        "continuous-heavy-rain" -> "Длительный сильный дождь"
        "showers" -> "Ливень"
        "wet-snow" -> "Дождь со снегом"
        "light-snow" -> "Небольшой снег"
        "snow" -> "Снег"
        "snow-showers" -> "Снегопад"
        "hail" -> "Град"
        "thunderstorm" -> "Град"
        "thunderstorm-with-rain" -> "Дождь с грозой"
        "thunderstorm-with-hail" -> "Гроза с градом"
        else -> condition
    }
}