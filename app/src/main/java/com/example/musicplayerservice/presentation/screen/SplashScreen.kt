package com.example.musicplayerservice.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayerservice.R
import com.example.musicplayerservice.utils.MyAppManager
import com.example.musicplayerservice.utils.checkPermissions
import com.example.musicplayerservice.utils.getMusicsCursor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.screen_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().checkPermissions(arrayOf(Manifest.permission.READ_MEDIA_AUDIO)) {
                requireContext().getMusicsCursor().onEach {
                    MyAppManager.cursor = it
                    findNavController().navigate(R.id.action_splashScreen_to_allMusicScreen)

                }.launchIn(lifecycleScope)
            }
        }

    }


}