package com.example.assignment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.assignment2.databinding.FragmentImageScaleBinding

class ImageScaleFragment : Fragment() {

    private var _binding: FragmentImageScaleBinding? = null
    private val binding get() = _binding!!

    // Reliable high-quality image URL from internet
    private val imageUrl = "https://picsum.photos/id/1018/1000/1000"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageScaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBarImage.visibility = View.VISIBLE

        binding.zoomImageView.load(imageUrl) {
            crossfade(true)
            listener(
                onSuccess = { _, _ ->
                    binding.progressBarImage.visibility = View.GONE
                },
                onError = { _, _ ->
                    binding.progressBarImage.visibility = View.GONE
                }
            )
        }

        binding.btnZoomIn.setOnClickListener {
            binding.zoomImageView.zoomIn()
        }

        binding.btnZoomOut.setOnClickListener {
            binding.zoomImageView.zoomOut()
        }

        binding.btnResetZoom.setOnClickListener {
            binding.zoomImageView.resetZoom()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}