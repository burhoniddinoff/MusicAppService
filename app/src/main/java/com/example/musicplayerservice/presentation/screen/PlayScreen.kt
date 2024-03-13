package com.example.musicplayerservice.presentation.screen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicplayerservice.R
import com.example.musicplayerservice.data.ActionEnum
import com.example.musicplayerservice.data.MusicData
import com.example.musicplayerservice.databinding.ScreenPlayBinding
import com.example.musicplayerservice.presentation.service.MyService
import com.example.musicplayerservice.utils.Constants
import com.example.musicplayerservice.utils.MyAppManager
import com.example.musicplayerservice.utils.setChangeProgress

class PlayScreen : Fragment(R.layout.screen_play) {
    private val binding by viewBinding(ScreenPlayBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ibForwardSong.setOnClickListener { startMyService(ActionEnum.NEXT) }
        binding.ibBackwardSong.setOnClickListener { startMyService(ActionEnum.PREV) }
        binding.ibPlay.setOnClickListener { startMyService(ActionEnum.MANAGE) }

        binding.seekBar.setChangeProgress { progress, fromUser ->
            if (fromUser) {
                val newPosition = progress * MyAppManager.fullTime / 100
                MyAppManager.currentTime = newPosition
                MyAppManager.isChanged = true
                MyAppManager.currentTimeLiveData.postValue(newPosition)
                binding.tvCurrentTime.text = Constants.durationConverter(newPosition)
            }
        }

        MyAppManager.playMusicLiveData.observe(viewLifecycleOwner, playMusicObserver)
        MyAppManager.isPlayingLiveData.observe(viewLifecycleOwner, isPlayingObserver)
        MyAppManager.currentTimeLiveData.observe(viewLifecycleOwner, currentTimeObserver)
    }

    private val playMusicObserver = Observer<MusicData> {

//        binding.seekBar.progress = MyAppManager.currentTime.toInt()
//        binding.seekBar.max = MyAppManager.fullTime.toInt()
//        binding.tvTitle.text = it.title
//        binding.tvAuthor.text = it.artist
//        binding.ibCover.setImageBitmap(it.image)
//
//        binding.tvCurrentTime.text = Constants.durationConverter(MyAppManager.currentTime)
//        binding.tvDuration.text = Constants.durationConverter(it.duration)

        binding.seekBar.progress = (MyAppManager.currentTime * 100 / MyAppManager.fullTime).toInt()
        binding.tvTitle.text = it.title
        binding.tvAuthor.text = it.artist
        binding.tvCurrentTime.text = MyAppManager.currentTime.toString()
        binding.tvDuration.text = Constants.durationConverter(it.duration)

    }


    private val isPlayingObserver = Observer<Boolean> {
        if (it) binding.ibPlay.setImageResource(R.drawable.ic_pause_2)
        else binding.ibPlay.setImageResource(R.drawable.ic_play_2)
    }

    private val currentTimeObserver = Observer<Long> {
        binding.seekBar.progress = (MyAppManager.currentTime * 100 / MyAppManager.fullTime).toInt()
        binding.tvCurrentTime.text = Constants.durationConverter(MyAppManager.currentTime)
    }

    private fun startMyService(action: ActionEnum) {
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", action)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }
}