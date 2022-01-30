package ru.dkotik.weatherapplication.view.thread

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ChangeConnectivityBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("СООБЩЕНИЕ ОБ ИЗМЕНЕНИИ ТИПА СОЕДИНЕНИЯ\n")
            append("Action: ${intent.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}