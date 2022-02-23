package ru.dkotik.weatherapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dkotik.weatherapplication.model.AppState
import ru.dkotik.weatherapplication.repository.Repository
import ru.dkotik.weatherapplication.repository.impl.RepositoryImpl

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
                    private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            var repeatLimit = 2
            var repeatCurrent = 0
            while (repeatCurrent != repeatLimit) {
                try {
                    liveDataToObserve.postValue(
                        AppState.Success(
                        if (isRussian) {
                            repositoryImpl.getWeatherFromLocalStorageRus()
                        } else {
                            repositoryImpl.getWeatherFromLocalStorageWorld()
                        }
                    ))
                    break
                } catch (e: Exception) {
                    e.printStackTrace()
                    repeatCurrent++
                    if (repeatCurrent == repeatLimit) {
                        liveDataToObserve.postValue(AppState.Error(RuntimeException("Ошибка подключения к серверу. Попробуйте еще раз")))
                    }
                }
            }
        }.start()
    }
}