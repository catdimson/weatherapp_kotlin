package ru.dkotik.weatherapplication.viewmodel.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dkotik.weatherapplication.App.Companion.getHistoryDao
import ru.dkotik.weatherapplication.model.AppState
import ru.dkotik.weatherapplication.repository.LocalRepository
import ru.dkotik.weatherapplication.repository.impl.LocalRepositoryImpl

class HistoryViewModel (
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepository.getAllHistory())
    }

}