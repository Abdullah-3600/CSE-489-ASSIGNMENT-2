package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomBroadcastReceiver(
    private val onMessageReceived: ((String) -> Unit)? = null
) : BroadcastReceiver() {

    companion object {
        const val ACTION_CUSTOM_BROADCAST = "com.example.assignment2.CUSTOM_BROADCAST"
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_CUSTOM_BROADCAST) {
            val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "No message received"
            onMessageReceived?.invoke(message)
            context?.let {
                Toast.makeText(it, "Custom Broadcast Received: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}