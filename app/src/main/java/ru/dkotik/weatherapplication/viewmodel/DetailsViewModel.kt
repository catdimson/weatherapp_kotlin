package ru.dkotik.weatherapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dkotik.weatherapplication.model.FactDTO
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.model.WeatherDTO
import ru.dkotik.weatherapplication.model.getDefaultCity
import ru.dkotik.weatherapplication.repository.DetailsRepository
import ru.dkotik.weatherapplication.repository.RemoteDataSource
import ru.dkotik.weatherapplication.repository.impl.DetailsRepositoryImpl
import ru.dkotik.weatherapplication.model.AppState
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

        fun getLiveData() = detailsLiveData

        fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
            detailsLiveData.value = AppState.Loading
            detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
        }

        private val callBack = object : Callback<WeatherDTO> {

            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                val serverResponse: WeatherDTO? = response.body()
                detailsLiveData.postValue(
                    if (response.isSuccessful && serverResponse != null) {
                        checkResponse(serverResponse)
                    } else {
                        AppState.Error(Throwable(SERVER_ERROR))
                    }
                )
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                detailsLiveData.postValue(AppState.Error(Throwable(t.message ?:
                REQUEST_ERROR)))
            }

            private fun checkResponse(serverResponse: WeatherDTO): AppState {
                val fact = serverResponse.fact
                return if (fact == null || fact.temp == null || fact.feelLike ==
                    null || fact.condition.isNullOrEmpty()) {
                    AppState.Error(Throwable(CORRUPTED_DATA))
                } else {
                    AppState.Success(convertDtoToModel(serverResponse))
                }
            }
        }

    fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
        val fact: FactDTO = weatherDTO.fact!!
        return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feelLike!!, fact.condition!!))
    }

}