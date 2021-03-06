package ru.dkotik.weatherapplication.model

sealed class AppState {

    data class Success(val weatherData: List<Weather>): AppState()

    data class Error(val error: Throwable): AppState()

    object Loading: AppState()

}
