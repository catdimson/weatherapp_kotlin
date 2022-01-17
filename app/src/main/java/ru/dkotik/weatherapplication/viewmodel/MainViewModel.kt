package ru.dkotik.weatherapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dkotik.weatherapplication.repository.Repository
import ru.dkotik.weatherapplication.repository.impl.RepositoryImpl
import java.lang.RuntimeException
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(3000)
            if (generateFakeError()) {
                liveDataToObserve.postValue(AppState.Success(
                    if (isRussian) {
                        repositoryImpl.getWeatherFromLocalStorageRus()
                    } else {
                        repositoryImpl.getWeatherFromLocalStorageWorld()
                    }
                ))
            } else {
                liveDataToObserve.postValue(AppState.Error(RuntimeException("Ошибка подключения к серверу. Попробуйте еще раз")))
            }
        }.start()
    }

    private fun generateFakeError(): Boolean {
        val randomInt = (0..11).random()
        return randomInt > 1
    }
}