package ru.dkotik.weatherapplication.view.thread

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fragment_threads.*
import ru.dkotik.weatherapplication.R
import ru.dkotik.weatherapplication.databinding.FragmentThreadsBinding
import java.util.*
import java.util.concurrent.TimeUnit

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val THREADS_FRAGMENT_BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"

class ThreadsFragment : Fragment() {

    private var counterThread = 0

    private var _binding: FragmentThreadsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context?.registerReceiver(testReceiver, IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(testReceiver, IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initServiceButton()
        initServiceWithBroadcastButton()

        binding.button.setOnClickListener {
            binding.textView.text =
                startCalculations(binding.editText.text.toString().toInt())
            binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                text = getString(R.string.in_main_thread)
                textSize =
                    resources.getDimension(R.dimen.main_container_text_size)
            })
        }

//        var handler = Handler(Looper.getMainLooper())

        binding.calcThreadBtn.setOnClickListener {
            Thread {
                counterThread++
                val calculatedText = startCalculations(editText.text.toString().toInt())

//                activity?.runOnUiThread(Runnable {
//                    binding.textView.text = calculatedText
//                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
//                        text = String.format(getString(R.string.from_thread), counterThread)
//                        textSize = resources.getDimension(R.dimen.main_container_text_size)
//                    })
//                })

//                binding.textView.post(Runnable {
//                    binding.textView.text = calculatedText
//                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
//                        text = String.format(getString(R.string.from_thread), counterThread)
//                        textSize = resources.getDimension(R.dimen.main_container_text_size)
//                    })
//                })

//                handler.post(Runnable {
//                    binding.textView.text = calculatedText
//                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
//                        text = String.format(getString(R.string.from_thread), counterThread)
//                        textSize = resources.getDimension(R.dimen.main_container_text_size)
//                    })
//                })

            }.start()
        }

        val handlerThread = HandlerThread(getString(R.string.my_handler_thread))
        handlerThread.start()

        val handler = Handler(handlerThread.looper)
        calcThreadHandler.setOnClickListener {
            mainContainer.addView(AppCompatTextView(it.context).apply {
                text = String.format(
                    getString(R.string.calculate_in_thread),
                    handlerThread.name
                )
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })
            handler.post {
                startCalculations(binding.editText.text.toString().toInt())
                mainContainer.post {
                    mainContainer.addView(AppCompatTextView(it.context).apply {
                        text = String.format(
                            getString(R.string.calculate_in_thread),
                            Thread.currentThread().name
                        )
                        textSize =
                            resources.getDimension(R.dimen.main_container_text_size)
                    })
                }
            }
        }
    }


    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    private fun initServiceButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
        }
    }

    //Создаём свой BroadcastReceiver (получатель широковещательного сообщения)
    private val testReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//Достаём данные из интента
            intent.getStringExtra(THREADS_FRAGMENT_BROADCAST_EXTRA)?.let {
//                addView(context, it)
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceWithBroadcastButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_INT_EXTRA,
                        binding.editText.text.toString().toInt()
                    )
                    putExtra(
                        MAIN_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
        }
    }

    override fun onDestroy() {
//        context?.unregisterReceiver(testReceiver)
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(testReceiver)
        }

        super.onDestroy()
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }

    class MyThread : Thread() {
        override fun run() {
            Looper.prepare()
            Looper.loop()
        }
    }
}