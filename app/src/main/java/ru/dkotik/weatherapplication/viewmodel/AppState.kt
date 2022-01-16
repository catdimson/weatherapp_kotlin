package ru.dkotik.weatherapplication.viewmodel

import ru.dkotik.weatherapplication.model.Weather

sealed class AppState {

    data class Success(val weatherData: Weather): AppState()

    data class Error(val error: Throwable): AppState()

    object Loading: AppState()

}
