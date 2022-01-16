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

    fun getWeatherFromLocalStore() = getDataFromLocalSource()

    fun getWeatherFromRemoteStore() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            if (generateFakeError()) {
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(RuntimeException("Ошибка подключения к серверу. Попробуйте еще раз")))
            }
        }.start()
    }

    private fun generateFakeError(): Boolean {
        val randomInt = (0..11).random()
        return randomInt > 5
    }
}