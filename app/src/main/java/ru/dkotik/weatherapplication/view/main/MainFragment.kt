package ru.dkotik.weatherapplication.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.FragmentMainBinding
import ru.dkotik.weatherapplication.model.Weather
import ru.dkotik.weatherapplication.showSnackBarWithResources
import ru.dkotik.weatherapplication.view.OnItemViewClickListener
import ru.dkotik.weatherapplication.view.details.DetailsFragment
import ru.dkotik.weatherapplication.model.AppState
import ru.dkotik.weatherapplication.utils.Constants
import ru.dkotik.weatherapplication.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var isDataSetRus: Boolean = true
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commit()
            }
        }
    })

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener {
            onChangeTowns()
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })

        showListOfTowns()
    }

    private fun showListOfTowns() {
        activity?.let {
            isDataSetRus = !it.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_WORLD_KEY, false)
            getTowns()
        }
    }

    private fun getTowns() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceRus()
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
        } else {
            viewModel.getWeatherFromLocalSourceRus()
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun onChangeTowns() {
        changeWeatherDataSet()
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putBoolean(Constants.IS_WORLD_KEY, !isDataSetRus)
        editor?.apply()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.isVisible = true
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                binding.mainFragmentRecyclerView.isVisible = false
            }
            is AppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.isVisible = false
                binding.mainFragmentFAB.showSnackBarWithResources(
                    fragment = this,
                    text = R.string.error,
                    actionText = R.string.reload,
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        adapter.removeListener()
        super.onDestroy()
    }
}