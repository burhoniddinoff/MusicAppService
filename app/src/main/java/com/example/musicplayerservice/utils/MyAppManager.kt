package com.example.musicplayerservice.utils

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import com.example.musicplayerservice.data.ActionEnum
import com.example.musicplayerservice.data.MusicData

object MyAppManager {
    var selectMusicPos: Int = -1
    var cursor: Cursor? = null
    var lastCommand: ActionEnum = ActionEnum.PLAY

    var currentTime: Long = 0L
    var fullTime: Long = 0L

    val currentTimeLiveData = MutableLiveData<Long>()

    val playMusicLiveData = MutableLiveData<MusicData>()
    val isPlayingLiveData = MutableLiveData<Boolean>()
    var isRepeatLiveData = MutableLiveData<Boolean>()
}