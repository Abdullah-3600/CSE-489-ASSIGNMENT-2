package com.example.assignment2

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private var broadcastType: String = SecondActivity.TYPE_CUSTOM
    private var customReceiver: CustomBroadcastReceiver? = null

    companion object {
        const val EXTRA_BROADCAST_TYPE = "extra_broadcast_type"
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarThird)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarThird.setNavigationOnClickListener {
            finish()
        }

        broadcastType = intent.getStringExtra(EXTRA_BROADCAST_TYPE) ?: SecondActivity.TYPE_CUSTOM

        if (broadcastType == SecondActivity.TYPE_BATTERY) {
            setupBatteryUi()
        } else {
            setupCustomUi()
        }
    }

    private fun setupCustomUi() {
        binding.containerCustomThird.visibility = View.VISIBLE
        binding.containerBatteryThird.visibility = View.GONE
        supportActionBar?.title = "Third Activity - Receiver"

        val directMessage = intent.getStringExtra(EXTRA_MESSAGE)
        if (!directMessage.isNullOrEmpty()) {
            binding.tvReceivedMessage.text = directMessage
        }

        // Register custom broadcast receiver to dynamically receive broadcasts in 3rd activity
        customReceiver = CustomBroadcastReceiver { receivedMsg ->
            binding.tvReceivedMessage.text = receivedMsg
        }

        val filter = IntentFilter(CustomBroadcastReceiver.ACTION_CUSTOM_BROADCAST)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(customReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(customReceiver, filter)
        }
    }

    private fun setupBatteryUi() {
        binding.containerCustomThird.visibility = View.GONE
        binding.containerBatteryThird.visibility = View.VISIBLE
        supportActionBar?.title = "Third Activity - Battery Option"
    }

    override fun onDestroy() {
        super.onDestroy()
        customReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                // Ignore if not registered
            }
        }
    }
}