package com.example.musicplayerservice.utils

import android.widget.SeekBar
import java.util.concurrent.TimeUnit

fun SeekBar.setChangeProgress(block: (progress: Int, fromUser: Boolean) -> Unit) {
    this.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            seekBar?.let { block.invoke(it.progress, fromUser) }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })
}

fun Long.durationConverter(): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )
}
