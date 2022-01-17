package ru.dkotik.weatherapplication.view.details

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
import ru.dkotik.weatherapplication.databinding.FragmentDetailsBinding
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.viewmodel.AppState
import ru.dkotik.weatherapplication.viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var mainView: ConstraintLayout
    private lateinit var city: TextView
    private lateinit var coordinates: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root // root == getRoot()
        findsViews()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if (weather != null) {
            val city = weather.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
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
        mainView = binding.mainView
        city = binding.cityName
        coordinates = binding.cityCoordinates
        temperature = binding.temperatureValue
        feelsLike = binding.feelsLikeValue
    }
}