package ru.dkotik.weatherapplication.repository.impl

import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.model.getRussianCities
import ru.dkotik.weatherapplication.model.getWorldCities
import ru.dkotik.weatherapplication.repository.Repository

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}