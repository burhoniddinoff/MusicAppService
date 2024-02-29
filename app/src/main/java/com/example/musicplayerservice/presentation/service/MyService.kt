package com.example.musicplayerservice.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.musicplayerservice.R
import com.example.musicplayerservice.data.ActionEnum
import com.example.musicplayerservice.data.MusicData
import com.example.musicplayerservice.utils.MyAppManager
import com.example.musicplayerservice.utils.getMusicDataByPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

class MyService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    private val channel = "DEMO"
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer get() = _mediaPlayer!!
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        _mediaPlayer = MediaPlayer()
        createChannel()
        startMyService()

    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("DEMO", channel, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)

            val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
        }
    }

    private fun startMyService() {
        val notification: Notification = NotificationCompat.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_shuffle)
            .setContentText("Hello bro!")
            .setContentTitle("What's up?")
            .setCustomContentView(createRemoteView())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()

        startForeground(1, notification)
    }

    private fun createRemoteView(): RemoteViews {
        val view = RemoteViews(this.packageName, R.layout.remote_view)
        val musicData = MyAppManager.cursor?.getMusicDataByPosition(MyAppManager.selectMusicPos)!!
        view.setTextViewText(R.id.textMusicName, musicData.title)
        view.setTextViewText(R.id.textArtistName, musicData.artist)
        if (mediaPlayer.isPlaying) {
            view.setImageViewResource(R.id.buttonManage, R.drawable.ic_pause_2)
        } else view.setImageViewResource(R.id.buttonManage, R.drawable.ic_play_2)

        view.setOnClickPendingIntent(R.id.buttonPrev, createPendingIntent(ActionEnum.PREV))
        view.setOnClickPendingIntent(R.id.buttonManage, createPendingIntent(ActionEnum.MANAGE))
        view.setOnClickPendingIntent(R.id.buttonNext, createPendingIntent(ActionEnum.NEXT))
        view.setOnClickPendingIntent(R.id.buttonCancel, createPendingIntent(ActionEnum.CANCEL))
//        view.setOnClickPendingIntent(R.id.buttonRepeat, createPendingIntent(ActionEnum.REPEAT))
        return view
    }


    private fun createPendingIntent(enum: ActionEnum): PendingIntent? {
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("COMMAND", enum)
        return PendingIntent.getService(this, enum.pos, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startMyService()
        val command = intent!!.extras?.getSerializable("COMMAND") as ActionEnum
        doneCommand(command)
        return START_NOT_STICKY
    }

    private fun doneCommand(command: ActionEnum) {
        val data: MusicData = MyAppManager.cursor?.getMusicDataByPosition(MyAppManager.selectMusicPos)!!
        when (command) {

            ActionEnum.MANAGE -> {
                if (mediaPlayer.isPlaying) doneCommand(ActionEnum.PAUSE)
                else doneCommand(ActionEnum.PLAY)
            }

            ActionEnum.PLAY -> {
                if (mediaPlayer.isPlaying)
                    mediaPlayer.stop()
                _mediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(data.data ?: "")))
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener { doneCommand(ActionEnum.NEXT) }
                MyAppManager.fullTime = data.duration
                mediaPlayer.seekTo(MyAppManager.currentTime.toInt())
                job?.cancel()

                job = scope.launch {
                    changeProgress().collectLatest {
                        MyAppManager.currentTime = it
                        MyAppManager.currentTimeLiveData.postValue(it)
                    }
                }

                MyAppManager.isPlayingLiveData.value = true
                MyAppManager.playMusicLiveData.value = data
                startMyService()
            }

            ActionEnum.PAUSE -> {
                mediaPlayer.stop()
                job?.cancel()
                MyAppManager.isPlayingLiveData.value = false
                startMyService()
            }

            ActionEnum.NEXT -> {
                MyAppManager.currentTime = 0
                if (MyAppManager.selectMusicPos + 1 == MyAppManager.cursor!!.count) MyAppManager.selectMusicPos = 0
                else MyAppManager.selectMusicPos++
                doneCommand(ActionEnum.PLAY)
            }

            ActionEnum.PREV -> {
                MyAppManager.currentTime = 0
                if (MyAppManager.selectMusicPos == 0) MyAppManager.selectMusicPos = MyAppManager.cursor!!.count - 1
                else MyAppManager.selectMusicPos--
                doneCommand(ActionEnum.PLAY)
            }

            ActionEnum.CANCEL -> {
                mediaPlayer.stop()
                stopSelf()
            }

            ActionEnum.REPEAT -> {
                mediaPlayer.isLooping = !mediaPlayer.isLooping
            }

        }
    }

    private fun changeProgress(): Flow<Long> = flow {
        for (i in MyAppManager.currentTime until MyAppManager.fullTime step 1000) {
            delay(1000)
            emit(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


}