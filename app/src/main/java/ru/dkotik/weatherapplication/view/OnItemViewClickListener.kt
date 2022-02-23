package ru.dkotik.weatherapplication.view

import ru.dkotik.weatherapplication.model.Weather

interface OnItemViewClickListener {
    fun onItemViewClick(weather: Weather)
}