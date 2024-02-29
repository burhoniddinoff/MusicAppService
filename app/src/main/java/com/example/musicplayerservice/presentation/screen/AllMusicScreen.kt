package com.example.musicplayerservice.presentation.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicplayerservice.R
import com.example.musicplayerservice.data.ActionEnum
import com.example.musicplayerservice.data.MusicData
import com.example.musicplayerservice.databinding.ScreenAllMusicBinding
import com.example.musicplayerservice.presentation.adapter.MusicAdapter
import com.example.musicplayerservice.presentation.service.MyService
import com.example.musicplayerservice.utils.MyAppManager

class AllMusicScreen : Fragment(R.layout.screen_all_music) {

    private val binding by viewBinding(ScreenAllMusicBinding::bind)
    private val adapter = MusicAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRV()
        init()

    }


    private fun setUpRV() {
        binding.rvSongList.adapter = adapter
        binding.rvSongList.layoutManager = LinearLayoutManager(requireContext())
        adapter.cursor = MyAppManager.cursor
    }

    private fun init() {

        binding.bottomPart.setOnClickListener {
            findNavController().navigate(R.id.action_allMusicScreen_to_playScreen2)
        }

        binding.buttonNextScreen.setOnClickListener { startMyService(ActionEnum.NEXT) }
        binding.buttonPrevScreen.setOnClickListener { startMyService(ActionEnum.PREV) }
        binding.buttonManageScreen.setOnClickListener { startMyService(ActionEnum.MANAGE) }

        adapter.setSelectMusicPositionListener {
            MyAppManager.selectMusicPos = it
            startMyService(ActionEnum.PLAY)
        }

        MyAppManager.playMusicLiveData.observe(viewLifecycleOwner, playMusicObserver)
        MyAppManager.isPlayingLiveData.observe(viewLifecycleOwner, isPlayingObserver)

    }


    private fun startMyService(action: ActionEnum) {
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", action)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }

    private val playMusicObserver = Observer<MusicData> { data ->
        binding.apply {
            textMusicNameScreen.text = data.title
            textArtistNameScreen.text = data.artist
        }
    }

    private val isPlayingObserver = Observer<Boolean> {
        if (it) binding.buttonManageScreen.setImageResource(R.drawable.ic_pause_2)
        else binding.buttonManageScreen.setImageResource(R.drawable.ic_play_2)
    }


}