package ru.dkotik.weatherapplication.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.MainActivityBinding
import ru.dkotik.weatherapplication.view.main.MainFragment
import ru.dkotik.weatherapplication.view.thread.ChangeConnectivityBroadcastReceiver
import ru.dkotik.weatherapplication.view.thread.ThreadsFragment
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var connectivityReceiver: ChangeConnectivityBroadcastReceiver? = null
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            //showFragment(ThreadsFragment.newInstance())
            showMainFragment()
        }
        registerConnectivityReceiver();
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showMainFragment() {
        showFragment(MainFragment.newInstance())
    }

    private fun getConnectivityReceiver(): ChangeConnectivityBroadcastReceiver? {
        if (connectivityReceiver == null)
            connectivityReceiver = ChangeConnectivityBroadcastReceiver();

        return connectivityReceiver;
    }

    private fun registerConnectivityReceiver() {
        try {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            //filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            //filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            registerReceiver(getConnectivityReceiver(), filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}