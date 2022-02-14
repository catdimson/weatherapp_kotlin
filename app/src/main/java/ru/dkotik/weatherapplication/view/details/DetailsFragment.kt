package ru.dkotik.weatherapplication.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.FragmentDetailsBinding
import ru.dkotik.weatherapplication.model.FactDTO
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.model.WeatherDTO
import javax.net.ssl.HttpsURLConnection

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private lateinit var mainView: ConstraintLayout
    private lateinit var city: TextView
    private lateinit var coordinates: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle) =
            DetailsFragment().also { fragment -> fragment.arguments = bundle }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayWeather(
                    WeatherDTO(
                        FactDTO(
                            intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                            intent.getStringExtra(DETAILS_CONDITION_EXTRA)
                        )
                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root // root == getRoot()
        findsViews()
        return view
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        binding.mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feelLike
        val condition = fact.condition
        if (temp!! <= TEMP_INVALID || feelsLike!! <= FEELS_LIKE_INVALID || condition == null) {
            TODO(PROCESS_ERROR)
        } else {
            val city = weatherBundle.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = temp.toString()
            binding.feelsLikeValue.text = feelsLike.toString()
            binding.weatherCondition.text = condition
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weatherBundle.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weatherBundle.city.lon
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findsViews() {
        mainView = binding.mainView
        city = binding.cityName
        coordinates = binding.cityCoordinates
        temperature = binding.temperatureValue
        feelsLike = binding.feelsLikeValue
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }
}