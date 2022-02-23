package ru.dkotik.weatherapplication.repository.impl

import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.repository.LocalRepository
import ru.dkotik.weatherapplication.room.HistoryDao
import ru.dkotik.weatherapplication.utils.convertHistoryEntityToWeather
import ru.dkotik.weatherapplication.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao): LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }

}