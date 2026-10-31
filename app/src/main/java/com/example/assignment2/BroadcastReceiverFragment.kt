package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.assignment2.databinding.FragmentBroadcastReceiverBinding

class BroadcastReceiverFragment : Fragment() {

    private var _binding: FragmentBroadcastReceiverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBroadcastReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = arrayOf(
            "Custom broadcast receiver",
            "System battery notification receiver"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )
        binding.spinnerBroadcastType.adapter = adapter

        binding.btnProceed.setOnClickListener {
            val selectedPosition = binding.spinnerBroadcastType.selectedItemPosition
            val typeExtra = if (selectedPosition == 0) {
                SecondActivity.TYPE_CUSTOM
            } else {
                SecondActivity.TYPE_BATTERY
            }

            val intent = Intent(requireContext(), SecondActivity::class.java).apply {
                putExtra(SecondActivity.EXTRA_BROADCAST_TYPE, typeExtra)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}