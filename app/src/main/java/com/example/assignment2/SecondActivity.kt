package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private var broadcastType: String = TYPE_CUSTOM

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

                val pct = if (level != -1 && scale != -1) {
                    (level * 100 / scale.toFloat()).toInt()
                } else 0

                binding.tvBatteryPercentage.text = "$pct%"

                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL
                binding.tvBatteryStatus.text = if (isCharging) {
                    "Device Status: Charging ⚡"
                } else {
                    "Device Status: Discharging"
                }
            }
        }
    }

    companion object {
        const val EXTRA_BROADCAST_TYPE = "extra_broadcast_type"
        const val TYPE_CUSTOM = "CUSTOM"
        const val TYPE_BATTERY = "BATTERY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSecond)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSecond.setNavigationOnClickListener {
            finish()
        }

        broadcastType = intent.getStringExtra(EXTRA_BROADCAST_TYPE) ?: TYPE_CUSTOM

        if (broadcastType == TYPE_BATTERY) {
            setupBatteryUi()
        } else {
            setupCustomUi()
        }
    }

    private fun setupCustomUi() {
        binding.containerCustom.visibility = View.VISIBLE
        binding.containerBattery.visibility = View.GONE
        supportActionBar?.title = "Custom Broadcast Input"

        binding.btnSendCustomBroadcast.setOnClickListener {
            val userText = binding.etCustomInput.text?.toString()?.trim()
            if (userText.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter some text first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create and send custom broadcast
            val broadcastIntent = Intent(CustomBroadcastReceiver.ACTION_CUSTOM_BROADCAST).apply {
                putExtra(CustomBroadcastReceiver.EXTRA_MESSAGE, userText)
                setPackage(packageName)
            }
            sendBroadcast(broadcastIntent)

            // Open Third Activity with broadcast details
            val thirdIntent = Intent(this, ThirdActivity::class.java).apply {
                putExtra(ThirdActivity.EXTRA_BROADCAST_TYPE, TYPE_CUSTOM)
                putExtra(ThirdActivity.EXTRA_MESSAGE, userText)
            }
            startActivity(thirdIntent)
        }
    }

    private fun setupBatteryUi() {
        binding.containerCustom.visibility = View.GONE
        binding.containerBattery.visibility = View.VISIBLE
        supportActionBar?.title = "System Battery Receiver"

        // Register receiver for battery changes
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)

        binding.btnProceedToThirdBattery.setOnClickListener {
            val thirdIntent = Intent(this, ThirdActivity::class.java).apply {
                putExtra(ThirdActivity.EXTRA_BROADCAST_TYPE, TYPE_BATTERY)
            }
            startActivity(thirdIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastType == TYPE_BATTERY) {
            try {
                unregisterReceiver(batteryReceiver)
            } catch (e: Exception) {
                // Ignore if not registered
            }
        }
    }
}