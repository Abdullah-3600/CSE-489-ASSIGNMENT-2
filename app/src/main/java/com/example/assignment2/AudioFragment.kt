package com.example.assignment2

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.assignment2.databinding.FragmentAudioBinding
import java.util.Locale

class AudioFragment : Fragment() {

    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var updateSeekBarRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMediaPlayer()

        binding.btnPlayAudio.setOnClickListener {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    updateSeekBar()
                }
            }
        }

        binding.btnPauseAudio.setOnClickListener {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                }
            }
        }

        binding.btnStopAudio.setOnClickListener {
            mediaPlayer?.let { player ->
                if (player.isPlaying || player.currentPosition > 0) {
                    player.stop()
                    player.prepare()
                    player.seekTo(0)
                    binding.seekBarAudio.progress = 0
                    binding.tvCurrentTime.text = formatTime(0)
                }
            }
        }

        binding.seekBarAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    binding.tvCurrentTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sample_audio)
        mediaPlayer?.let { player ->
            binding.seekBarAudio.max = player.duration
            binding.tvTotalTime.text = formatTime(player.duration)
            binding.tvCurrentTime.text = formatTime(0)

            player.setOnCompletionListener {
                binding.seekBarAudio.progress = 0
                binding.tvCurrentTime.text = formatTime(0)
            }
        }
    }

    private fun updateSeekBar() {
        updateSeekBarRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        binding.seekBarAudio.progress = player.currentPosition
                        binding.tvCurrentTime.text = formatTime(player.currentPosition)
                        handler.postDelayed(this, 200)
                    }
                }
            }
        }
        handler.post(updateSeekBarRunnable!!)
    }

    private fun formatTime(millis: Int): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        updateSeekBarRunnable?.let { handler.removeCallbacks(it) }
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}