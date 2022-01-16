package ru.dkotik.weatherapplication.repository.impl

import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.repository.Repository

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }

}