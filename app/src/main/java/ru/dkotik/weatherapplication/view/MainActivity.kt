package ru.dkotik.weatherapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.MainActivityBinding
import ru.dkotik.weatherapplication.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commit()
        }
    }
}