package ru.dkotik.weatherapplication.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.MainFragmentBinding
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.viewmodel.AppState
import ru.dkotik.weatherapplication.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var loadingLayout: FrameLayout
    private lateinit var mainView: ConstraintLayout
    private lateinit var city: TextView
    private lateinit var coordinates: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root // root == getRoot()
        findsViews()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { state ->
            renderData(state)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getWeatherFromLocalStore()
    }

    private fun renderData(state: AppState?) {
        when (state) {
            is AppState.Success -> {
                val weatherData = state.weatherData
                loadingLayout.isVisible = false
                setData(weatherData)
                mainView.isVisible = true
                Snackbar.make(mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                Snackbar.make(mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") {
                        viewModel.getWeatherFromLocalStore()
                    }
                    .show()
                loadingLayout.isVisible = false
            }
            is AppState.Loading -> {
                loadingLayout.isVisible = true
                mainView.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(weatherData: Weather) {
        city.text = weatherData.city.city
        coordinates.text = String.format(getString(R.string.city_coordinates), weatherData.city.lat.toString(), weatherData.city.lon.toString())
        temperature.text = weatherData.temperature.toString()
        feelsLike.text = weatherData.feelsLike.toString()
    }

    private fun findsViews() {
        loadingLayout = binding.loadingLayout
        mainView = binding.mainView
        city = binding.cityName
        coordinates = binding.cityCoordinates
        temperature = binding.temperatureValue
        feelsLike = binding.feelsLikeValue
    }
}