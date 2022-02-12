package ru.dkotik.weatherapplication.repository.impl

import okhttp3.Callback
import ru.dkotik.weatherapplication.model.WeatherDTO
import ru.dkotik.weatherapplication.repository.DetailsRepository
import ru.dkotik.weatherapplication.repository.RemoteDataSource

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

}