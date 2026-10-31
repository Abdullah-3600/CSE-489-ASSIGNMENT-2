package com.example.assignment2

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.assignment2.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.sample_video)

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)

        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(videoUri)

        binding.videoView.setOnPreparedListener { mp ->
            binding.progressBarVideo.visibility = View.GONE
            mp.isLooping = true
            binding.videoView.start()
        }

        binding.btnPlayVideo.setOnClickListener {
            if (!binding.videoView.isPlaying) {
                binding.videoView.start()
            }
        }

        binding.btnPauseVideo.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
            }
        }

        binding.btnReplayVideo.setOnClickListener {
            binding.videoView.seekTo(0)
            binding.videoView.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding.videoView.isPlaying) {
            binding.videoView.stopPlayback()
        }
        _binding = null
    }
}