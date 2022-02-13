package ru.dkotik.weatherapplication.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.MainActivityBinding
import ru.dkotik.weatherapplication.view.history.HistoryFragment
import ru.dkotik.weatherapplication.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            showMainFragment()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showMainFragment() {
        showFragment(MainFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showFragment(HistoryFragment.newInstance())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}